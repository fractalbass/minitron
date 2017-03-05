package com.phg.minitron.integration

import groovy.util.logging.Slf4j

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface DockerDependent {
    @SuppressWarnings('FieldName') DockerUtil dockerUtil = new DockerUtil().start()
}

@Slf4j
class DockerUtil {
    static dockerUtil = null
    static Thread shutdownHook = null
    static String projectName = 'dockertest' + System.currentTimeMillis().toString()

    DockerUtil start() {
        Map env = System.getenv()
        if(env.containsKey('IGNORE_DOCKERDEPENDENT') && env.IGNORE_DOCKERDEPENDENT.toBoolean()) {
            log.debug "IGNORE_DOCKERDEPENDENT=\"${env.IGNORE_DOCKERDEPENDENT}\", skipping startup."
        } else {
            Process dockerProc
            def dockerFile = this.getClass().getResource('/docker-compose.yml').text
            if (!dockerUtil) {
                try {
                    dockerProc = ['docker-compose', '-p', projectName, '-f', '-', 'up', '-d'].execute()
                    dockerProc.out.write(dockerFile.bytes)
                    dockerProc.out.close()
                    dockerProc.waitFor()
                    if (dockerProc.exitValue()) {
                        log.error "docker-compose exited with error: ${dockerProc.errorStream.text}"
                    } else {
                        dockerUtil = { -> this.cleanup() }
                        registerWithJVMShutdown()
                        log.debug "${getClass().simpleName} : waiting for docker services to start"
                        sleep 60000 // lame!
                    }
                } catch (any) {
                    log.error "caught ${any.class.simpleName} trying to start docker resource: ${any.message}"
                }
            }
        }

        this
    }

    void cleanup() {
        getServicesInProject().each { serviceName ->
            stopService(serviceName)
            removeService(serviceName)
        }
        cleanupDocker()
    }

    void registerWithJVMShutdown() {
        shutdownHook = new Thread() {
            void run() {
                println "JVM shutting down, cleaning up DockerUtil resources"
                DockerUtil.dockerUtil()
            }
        }
        Runtime.getRuntime().addShutdownHook(shutdownHook)
    }

    int dockerCommandRunner(ArrayList<String> commandArray) {
        log.debug ">>> ${commandArray.join(' ')}"
        Process runner = commandArray.execute()
        runner.waitFor()
        int exitValue = runner.exitValue()
        runner.destroyForcibly()
        exitValue
    }

    void stopService(String serviceName) {
        log.debug "stopping ${serviceName}"
        if(dockerCommandRunner(['docker', 'stop', "${serviceName}"])) {
            log.debug "failed to stop service ${serviceName}"
        }
    }

    void removeService(String serviceName) {
        log.debug "removing ${serviceName}"
        if(dockerCommandRunner(['docker', 'rm', "${serviceName}"])) {
            log.debug "failed to remove service ${serviceName}:"
        }
    }

    void removeNetwork() {
        if(dockerCommandRunner(['docker', 'network', 'rm', "${projectName}_default"])) {
            log.debug "failed to remove network ${projectName}_default"
        }
    }

    ArrayList<String> getDanglingVolumes() {
        String[] volumes = []
        Process runner = ['docker', 'volume', 'ls', '-q', '-f', 'dangling=true'].execute()
        runner.waitFor()
        if(!runner.exitValue()) {
            volumes = runner.in.text.replaceAll(/\s+/,' ').split(' ')
        }
        runner.destroyForcibly()
        volumes
    }

    void removeDanglingVolumes() {
        def danglingVolumes = getDanglingVolumes()
        if(!danglingVolumes.isEmpty()) {
            if (dockerCommandRunner(['docker', 'volume', 'rm'] + danglingVolumes)) {
                log.debug "failed to remove dangling volumes"
            }
        }
    }

    void cleanupDocker() {
        try {
            removeNetwork()
            removeDanglingVolumes()
        } catch(any) {}
    }

    List getServicesInProject() {
        List services = null
        Process runner = ['docker', 'ps', '-f', "name=${projectName}_*", '--format', '{{.Names}}'].execute()
        runner.waitFor()
        if(!runner.exitValue()) {
            services = runner.in.text.replaceAll(/\s+/, ' ').split(' ')
        }
        runner.destroyForcibly()
        services
    }
}
