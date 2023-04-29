import { IActionPlan } from 'app/shared/model/action-plan.model';

export interface IPhoneLink {
  id?: number;
  number?: string | null;
  name?: string | null;
  actionPlan?: IActionPlan | null;
}

export const defaultValue: Readonly<IPhoneLink> = {};
