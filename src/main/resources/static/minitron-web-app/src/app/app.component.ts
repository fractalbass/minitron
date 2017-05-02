import { Component } from '@angular/core';
import { Device } from './device'
import { User } from './user'
import { Message } from './message'

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
  '<li *ngFor="#device of devices">' +
  '{{device?.id}}</li>' +
  '</div>'
})

export class AppComponent {
  title = 'Minitron Management Web Application';
  devices = DEVICES;
  user: User = {
    email:'',
    passwd:''
  }
  selectedDevice = new Device;

  showDevices(): void {
    alert("logged in.");
    this.devices = DEVICES;
  }

  expand(device: Device): void {
    alert("Selected");

    device.messages = MESSAGES;
  }
}

const MESSAGES: Message[] = [
  {id: '1', messageText: 'Message 1'},
  {id: '2', messageText: 'Message 2'},
  {id: '3', messageText: 'Message 3'},
  {id: '4', messageText: 'Message 4'},
  {id: '5', messageText: 'Message 5'},
  {id: '6', messageText: 'Message 6'},
  {id: '7', messageText: 'Message 7'},
  {id: '8', messageText: 'Message 8'},
  {id: '9', messageText: 'Message 9'},
  {id: '10', messageText: 'Message 10'}
];

const DEVICES: Device[] = [
  { id: '11', code: 'abc123', owner: 'mporter@blah.com',messages: [], show:false},
  { id: '12', code: 'abc124', owner: 'mporter@blah.com',messages: MESSAGES, show:false},
  { id: '13', code: 'abc125', owner: 'mporter@blah.com',messages: [] , show:false},
  { id: '14', code: 'abc126', owner: 'mporter@blah.com',messages: [] , show:false},
  { id: '15', code: 'abc127', owner: 'mporter@blah.com',messages: [], show:false},
  { id: '16', code: 'abc128', owner: 'mporter@blah.com',messages: [], show:false},
  { id: '17', code: 'abc129', owner: 'mporter@blah.com',messages: [], show:false},
  { id: '18', code: 'abc120', owner: 'mporter@blah.com',messages: [], show:false},
  { id: '19', code: 'abc121', owner: 'mporter@blah.com',messages: [], show:false},
  { id: '20', code: 'abc122', owner: 'mporter@blah.com',messages: [], show:false}
];





