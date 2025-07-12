export default interface Bus {
  busId: number;
  busName: string;
  busType: string;
  sittingCapacity: number;
  typeSleeperCapacity: number;
  typeSittingCapacity: number;
  busSource: string;
  busDestination: string;
  busDuration: number;
  departureFromSource: string;
  arrivalOnDestination: string;
  priceSitting: number;
  priceSleeper: number;
  busDate: string;
  // images: { url: string }[];
}
