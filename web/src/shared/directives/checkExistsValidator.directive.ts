import {AbstractControl, ValidatorFn} from "@angular/forms";
import {reduceString} from "../utils/reduceString";

export function checkExistsValidator(objects: any[], key: string): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } | null => {
    if (objects.find(l => {
      return reduceString(control.value[key])
        ===
        reduceString(l[key])
    })) {
      return null;
    }
    return {'checkExistsValidator': {value: control.value}}
  };
}
