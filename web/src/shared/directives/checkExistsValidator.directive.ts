import {AbstractControl, ValidatorFn} from "@angular/forms";

export function checkExistsValidator(objects: any[], key: string): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } | null => {
    if (objects.find(l => {
      return control.value[key]
          .toLowerCase()
          .normalize("NFD").replace(/[\u0300-\u036f]/g, "") // Para remover las tildes
          .trim()
          .replace(/\s/g, '') // Para remover los espacios dentro del string
        ===
        l[key]
          .toLowerCase()
          .normalize("NFD").replace(/[\u0300-\u036f]/g, "") // Para remover las tildes
          .trim()
          .replace(/\s/g, '') // Para remover los espacios dentro del string
    })) {
      return null;
    }
    return {'checkExistsValidator': {value: control.value}}
  };
}
