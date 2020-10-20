import {AbstractControl, ValidatorFn} from "@angular/forms";

export function alreadyExistsValidator(objects: any[], key: string): ValidatorFn {
  return (control: AbstractControl): {[key: string]: any} | null => {
    if(objects.find(l => control.value === l[key])) {
      return {'alreadyExistsValidator': {value: control.value}}
    }
    return null;
  };
}
