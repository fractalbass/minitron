import { Message } from './message';

export class Device {
  id: string;
  code: string;
  owner: string;
  messages: Message[];
  show: boolean;
}
