"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var core_1 = require('@angular/core');
var device_1 = require('./device');
var user_service_1 = require('./user.service');
var http_1 = require('@angular/http');
var MESSAGES = [
    { id: '1', messageText: 'Message 1' },
    { id: '2', messageText: 'Message 2' },
    { id: '3', messageText: 'Message 3' },
    { id: '4', messageText: 'Message 4' },
    { id: '5', messageText: 'Message 5' },
    { id: '6', messageText: 'Message 6' },
    { id: '7', messageText: 'Message 7' },
    { id: '8', messageText: 'Message 8' },
    { id: '9', messageText: 'Message 9' },
    { id: '10', messageText: 'Message 10' }
];
var DEVICES = [
    { id: '11', code: 'abc123', owner: 'mporter@blah.com', messages: [], show: false },
    { id: '12', code: 'abc124', owner: 'mporter@blah.com', messages: MESSAGES, show: false },
    { id: '13', code: 'abc125', owner: 'mporter@blah.com', messages: [], show: false },
    { id: '14', code: 'abc126', owner: 'mporter@blah.com', messages: [], show: false },
    { id: '15', code: 'abc127', owner: 'mporter@blah.com', messages: [], show: false },
    { id: '16', code: 'abc128', owner: 'mporter@blah.com', messages: [], show: false },
    { id: '17', code: 'abc129', owner: 'mporter@blah.com', messages: [], show: false },
    { id: '18', code: 'abc120', owner: 'mporter@blah.com', messages: [], show: false },
    { id: '19', code: 'abc121', owner: 'mporter@blah.com', messages: [], show: false },
    { id: '20', code: 'abc122', owner: 'mporter@blah.com', messages: [], show: false }
];
var AppComponent = (function () {
    function AppComponent() {
        this.title = 'Minitron Management Web Application';
        this.loggedOut = true;
        this.http = http_1.Http;
        this.devices = DEVICES;
        this.userService = user_service_1.UserService;
        this.user = {
            email: '',
            passwd: ''
        };
        this.selectedDevice = new device_1.Device;
    }
    AppComponent.prototype.showDevices = function () {
        this.userService.getUser(1);
        this.devices = DEVICES;
        this.loggedOut = false;
    };
    AppComponent.prototype.onSelect = function (device) {
        this.device = device;
    };
    AppComponent = __decorate([
        core_1.Component({
            selector: 'app-root',
            templateUrl: './app.component.html',
            styleUrls: ['./app.component.css'],
            template: "<h1>{{title}}</h1><hr>\n  <div *ngIf=\"loggedOut\">\n  <label>Please log in:</label><br>\n  <input size=\"30\" [(ngModel)]=\"user.email\" placeholder=\"email\"><br>\n  <input size=\"30\" [(ngModel)]=\"user.passwd\" type=\"password\" placeholder=\"password\"><br>\n  <button name=\"login\" (click)=\"showDevices()\">login</button><br><br>\n  </div>\n  <div *ngIf=\"!loggedOut\">\n  <table border=\"1\" cellpadding=\"10\">\n    <tr>\n    <td valign=\"top\" width=\"200\">\n    <h2>Devices:</h2>Click to select...<br>\n    <div *ngFor=\"let device of devices\"\n      [class.selected]=\"device === selectedDevice\"\n      (click)=\"onSelect(device)\">\n      <span class=\"badge\">{{device.id}}</span> {{device.code}}\n      <div *ngIf=\"\" hidden>Device details.</div></div>\n      </td>\n    <td valign=\"top\" width=\"600\"><h2>Device Details</h2>\n      <div *ngIf=\"device\">\n      <div>\n        <label>id: </label>{{device.id}}\n      </div>\n      <div>\n        <label>code: </label>{{device.code}}\n      </div><br>\n      <div>Configured Messages:</div>\n      <div *ngFor=\"let message of device.messages\"\n      [class.selected]=\"message === selectedMessage\"\n      (click)=\"onSelectMessage(message)\">\n      <span class=\"badge\">{{message.id}}</span>\n        <input [(ngModel)]=\"message.messageText\" placeholder=\"Message Text\"/></div>\n    </div>\n    </td>\n\n  </tr>\n  </table>\n  </div>\n  "
        })
    ], AppComponent);
    return AppComponent;
}());
exports.AppComponent = AppComponent;
//# sourceMappingURL=app.component.js.map