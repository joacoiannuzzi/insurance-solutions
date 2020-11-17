import {AbstractControl, ValidatorFn} from "@angular/forms";
import {reduceString} from "../utils/reduceString";

export function alreadyExistsValidator(objects: any[], key: string): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } | null => {
    if (objects.find(l =>
      reduceString(control.value)
      ===
      reduceString(l[key])
    )) {
      return {'alreadyExistsValidator': {value: control.value}}
    }
    return null;
  };
}
