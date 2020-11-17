import {AbstractControl, ValidatorFn} from "@angular/forms";

export function alreadyExistsValidator(objects: any[], key: string): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } | null => {
    if (objects.find(l =>
      control.value
        .toLowerCase()
        .normalize("NFD").replace(/[\u0300-\u036f]/g, "") // Para remover las tildes
        .trim()
        .replace(/\s/g,'') // Para remover los espacios dentro del string
      ===
      l[key]
        .toLowerCase()
        .normalize("NFD").replace(/[\u0300-\u036f]/g, "") // Para remover las tildes
        .trim()
        .replace(/\s/g,'') // Para remover los espacios dentro del string
    )) {
      return {'alreadyExistsValidator': {value: control.value}}
    }
    return null;
  };
}
