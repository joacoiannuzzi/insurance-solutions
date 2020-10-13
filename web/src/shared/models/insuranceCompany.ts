export class InsuranceCompany {
  id: number;
  name: string;

  constructor(id: number, name: string) {
    this.id = id;
    this.name = name;
  }

  static fromJsonObject(jsonObject: any): InsuranceCompany {
    return new InsuranceCompany(jsonObject.id, jsonObject.name);
  }
}
