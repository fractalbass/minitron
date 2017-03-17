import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  template: '<h1>{{title}}</h1><hr>' +
  '<label>Please log in:</label><br>' +
  '<input size="30" [(ngModel)]="user.email" placeholder="email"><br>' +
  '<input size="30" [(ngModel)]="user.passwd" type="password" placeholder="password"><br>' +
  '<button name="login" (click)="showDevices()">login</button><br><br>' +
  '<div>' +
  '<li *ngFor="let device of devices"' +
  '  [class.selected]="device === selectedDevice"' +
  '(click)="onSelect(device)">' +
  '<span class="badge">{{device.id}}</span> {{device.code}}' +
  '</li>' +
  '</div>'
})

export class AppComponent {
  title = 'Minitron Management Web Application';
  devices = new Array<Device>();
  user: User = {
    email:'',
    passwd:''
  }

  showDevices(): void {
    this.devices = DEVICES;
  }

  onSelect(device: Device): void {
    alert("boom" + device.code);
  }
}



export class User {
  email: string;
  passwd: string;
}

export class Device {
  id: string;
  code: string;
  owner: string;
}

const DEVICES: Device[] = [
  { id: '11', code: 'abc123', owner: 'mporter@blah.com' },
  { id: '12', code: 'abc124', owner: 'mporter@blah.com' },
  { id: '13', code: 'abc125', owner: 'mporter@blah.com' },
  { id: '14', code: 'abc126', owner: 'mporter@blah.com' },
  { id: '15', code: 'abc127', owner: 'mporter@blah.com' },
  { id: '16', code: 'abc128', owner: 'mporter@blah.com' },
  { id: '17', code: 'abc129', owner: 'mporter@blah.com' },
  { id: '18', code: 'abc120', owner: 'mporter@blah.com' },
  { id: '19', code: 'abc121', owner: 'mporter@blah.com' },
  { id: '20', code: 'abc122', owner: 'mporter@blah.com' }
];



