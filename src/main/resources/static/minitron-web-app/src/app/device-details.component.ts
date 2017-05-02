import { Component, Input } from '@angular/core';
import { Device } from './device';
import { Message } from './message';

@Component({
  selector: 'device-detail',
  template: `
    <div *ngIf="device">
      <h2>{{device.name}} details!</h2>
      <div><label>id: </label>{{device.id}}</div>
      <div>
        <label>code: </label>
        <input [(ngModel)]="device.code" placeholder="code"/>
      </div>
      <div>
        <label>owner: </label>
        <input [(ngModel)]="device.owner" placeholder="owner"/>
      </div>
      <div>
        <label>messages: </label>
          <li *ngFor="let message of device.messages"
          [class.selected]="message === selectedMessage"
          (click)="onSelect(message)">
          </li>
      </div>
  `
})

export class DeviceDetailComponent {
  @Input() device: Device;
}
