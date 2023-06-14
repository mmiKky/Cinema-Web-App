export interface CinemaHall {
  seatsList: Seat[];
  id: number;
  name: string;
}
export interface Seat {
  id?: number;
  row: number;
  seatNr: number;
  isDouble: boolean;
  price: number;
  available: boolean;
}
