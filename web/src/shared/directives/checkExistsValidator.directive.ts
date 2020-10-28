import {AbstractControl, ValidatorFn} from "@angular/forms";

export function checkExistsValidator(objects: any[], key: string): ValidatorFn {
  return (control: AbstractControl): {[key: string]: any} | null => {
    if(objects.find(l => {
      return control.value[key] === l[key];
    })) {
      return null;
    }
    return {'checkExistsValidator': {value: control.value}}
  };
}
