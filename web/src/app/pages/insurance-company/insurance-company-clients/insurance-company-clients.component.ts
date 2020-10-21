import {AfterViewInit, Component, Inject, OnInit, ViewChild} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {Client} from "../../../../shared/models/client";
import {MatSort} from "@angular/material/sort";
import {MatPaginator} from "@angular/material/paginator";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {InsuranceCompany} from "../../../../shared/models/insuranceCompany";
import {InsuranceCompanyService} from "../../../../shared/services/insurance-company.service";
import {ConfirmDialogComponent} from "../../../components/confirm-dialog/confirm-dialog.component";
import {ClientDetailsComponent} from "../../client/client-details/client-details.component";
import {ClientUpdateComponent} from "../../client/client-update/client-update.component";

@Component({
  selector: 'app-insurance-company-clients',
  templateUrl: './insurance-company-clients.component.html',
  styleUrls: ['./insurance-company-clients.component.scss']
})
export class InsuranceCompanyClientsComponent implements OnInit, AfterViewInit {
  displayedColumns: string[] = ['firstName', 'lastName', 'dni', 'options'];
  dataSource: MatTableDataSource<Client> = new MatTableDataSource<Client>([]);

  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;

  constructor(public dialogRef: MatDialogRef<InsuranceCompanyClientsComponent>,
              @Inject(MAT_DIALOG_DATA) public insuranceCompany: InsuranceCompany,
              public dialog: MatDialog,
              private insuranceCompanyService: InsuranceCompanyService) {

  }

  ngOnInit(): void {
    this.dataSource.data = this.insuranceCompany.clients;
    this.paginator._intl.itemsPerPageLabel = 'Elementos por página';
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  }

  closeClients() {
    this.dialogRef.close(this.insuranceCompany);
  }

  refreshInsuranceCompany() {
    this.insuranceCompanyService.insuranceCompanies.subscribe(data => {
      this.insuranceCompany = data.find(is => is.id === this.insuranceCompany.id);
      this.dataSource.data = this.insuranceCompany.clients;
    })
  }

  deleteClient(element: Client) {
    this.dialog.open(ConfirmDialogComponent, {
      width: '800px',
      data: "¿Esta seguro de que desea eliminar el cliente con dni " + element.dni + " de la empresa aseguradora " + this.insuranceCompany.name + "?"
    })
      .afterClosed()
      .subscribe((confirmed: Boolean) => {
        if (confirmed) {
          this.insuranceCompany.clients.splice(this.insuranceCompany.clients.findIndex(c => c.dni === element.dni), 1);
          this.insuranceCompanyService.update(this.insuranceCompany, 'El cliente fue desasignado de la empresa aseguradora con éxito.', 'Hubo un error al desasignar el cliente de la empresa aseguradora.').subscribe(
            () => {this.dataSource.data = this.insuranceCompany.clients},//res
            () => {this.refreshInsuranceCompany()})//err
        }
      });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  openClientDetails(element: Client) {
    const dialogRef = this.dialog.open(ClientDetailsComponent, {
      width: '800px',
      data: element
    });
    dialogRef.afterClosed().subscribe(() => {
      this.refreshInsuranceCompany();
    })
  }

  updateClient(element: Client) {
    const dialogRef = this.dialog.open(ClientUpdateComponent, {
      width: '800px',
      data: element
    });
    dialogRef.afterClosed().subscribe((res) => {
      if (res) {
        this.refreshInsuranceCompany();
      }
    })
  }
}
