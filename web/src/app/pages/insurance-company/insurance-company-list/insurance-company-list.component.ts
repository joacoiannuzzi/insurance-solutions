import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {MatSort} from "@angular/material/sort";
import {MatPaginator} from "@angular/material/paginator";
import {MatDialog} from "@angular/material/dialog";
import {InsuranceCompanyService} from "../../../../shared/services/insurance-company.service";
import {MatTableDataSource} from "@angular/material/table";
import {InsuranceCompany} from "../../../../shared/models/insuranceCompany";
import {InsuranceCompanyAddComponent} from "../insurance-company-add/insurance-company-add.component";
import {InsuranceCompanyUpdateComponent} from "../insurance-company-update/insurance-company-update.component";
import {InsuranceCompanyClientsComponent} from "../insurance-company-clients/insurance-company-clients.component";

@Component({
  selector: 'app-insurance-company-list',
  templateUrl: './insurance-company-list.component.html',
  styleUrls: ['./insurance-company-list.component.scss']
})
export class InsuranceCompanyListComponent implements OnInit, AfterViewInit {
  displayedColumns: string[] = ['name', 'options'];
  insuranceCompanies: InsuranceCompany[];
  dataSource: MatTableDataSource<InsuranceCompany> = new MatTableDataSource<InsuranceCompany>([]);
  loading: boolean = true;

  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;

  constructor(private insuranceCompanyService: InsuranceCompanyService, public dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.getInsuranceCompanies();
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    this.paginator._intl.itemsPerPageLabel = 'Elementos por página';
  }

  getInsuranceCompanies() {
    this.loading = true;
    this.insuranceCompanyService.insuranceCompanies.subscribe((data) => {
      this.insuranceCompanies = data;
      this.loading = false;
      this.dataSource.data = this.insuranceCompanies;
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(InsuranceCompanyAddComponent, {
      width: '800px',
      data: new InsuranceCompany(-1, "")
    });

    dialogRef.afterClosed().subscribe(() => {
      this.getInsuranceCompanies();
    });
  }

  deleteInsuranceCompany(insuranceCompany: InsuranceCompany) {
    /*this.dialog.open(ConfirmDialogComponent, {
      data: "¿Está seguro de que desea eliminar a la empresa aseguradora " + insuranceCompany.name + "?"
    })
      .afterClosed()
      .subscribe((confirmed: boolean) => {
        if (confirmed) {
          this.insuranceCompanyService.delete(insuranceCompany).subscribe(() => {
            this.getInsuranceCompanies();
          });
        }
      })*/
  }

  updateInsuranceCompany(insuranceCompany: InsuranceCompany) {
    const dialogRef = this.dialog.open(InsuranceCompanyUpdateComponent, {
      width: '800px',
      data: insuranceCompany
    });
    dialogRef.afterClosed().subscribe((res) => {
      if (res) {
        this.getInsuranceCompanies();
      }
    })
  }

  openInsuranceCompanyClients(element: InsuranceCompany): void {
    const dialogRef = this.dialog.open(InsuranceCompanyClientsComponent, {
      width: '800px',
      data: element
    });
    dialogRef.afterClosed().subscribe(() => {
      this.getInsuranceCompanies();
    })
  }
}
