export class TransactionDetails {
  paymentId: number;
  currency: string;
  amount: number;
  key: string;
  constructor() {
    this.paymentId = 0;
    this.currency = '';
    this.amount = 0;
    this.key = '';
  }
}
