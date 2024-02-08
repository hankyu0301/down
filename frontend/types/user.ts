import { SportType } from "./sport";

export interface Profile {
  userId: number;
  email: string;
  preferredLocation: string;
  preferredSport: SportType;
  createdAt: Date;
  updatedAt: Date;
}