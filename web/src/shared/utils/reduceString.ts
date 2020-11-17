export function reduceString(str: string) {
  return str?.toLowerCase()
    .normalize("NFD").replace(/[\u0300-\u036f]/g, "") // Para remover las tildes
    .trim()
    .replace(/\s/g,'') // Para remover los espacios dentro del string
}
