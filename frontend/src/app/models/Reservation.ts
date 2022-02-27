export class Reservation {
  firstName: string = '';
  lastName: string = '';
  date: string = '';
  hour: string = '';

  constructor(firstName: string, lastName: string, date: string, hour: string) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.date = date;
    this.hour = hour;
  }
}
