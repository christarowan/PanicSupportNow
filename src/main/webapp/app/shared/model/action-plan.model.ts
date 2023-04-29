import { ISoundtrack } from 'app/shared/model/soundtrack.model';
import { ICopingStrategies } from 'app/shared/model/coping-strategies.model';
import { IPhoneLink } from 'app/shared/model/phone-link.model';

export interface IActionPlan {
  id?: number;
  soundtrack?: ISoundtrack | null;
  copingStrategies?: ICopingStrategies | null;
  phoneLinks?: IPhoneLink[] | null;
}

export const defaultValue: Readonly<IActionPlan> = {};
