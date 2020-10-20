import {AfterViewInit, Component, Inject, OnInit, ViewChild} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {Client} from "../../../../shared/models/client";
import {MatSort} from "@angular/material/sort";
import {MatPaginator} from "@angular/material/paginator";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {ClientService} from "../../../../shared/services/client.service";
import {InsuranceCompany} from "../../../../shared/models/insuranceCompany";
import {InsuranceCompanyService} from "../../../../shared/services/insurance-company.service";
import {ConfirmDialogComponent} from "../../../components/confirm-dialog/confirm-dialog.component";

@Component({
  selector: 'app-insurance-company-clients',
  templateUrl: './insurance-company-clients.component.html',
  styleUrls: ['./insurance-company-clients.component.scss']
})
export class InsuranceCompanyClientsComponent implements OnInit, AfterViewInit {
  displayedColumns: string[] = ['name', 'dni', 'options'];
  dataSource: MatTableDataSource<Client> = new MatTableDataSource<Client>([]);
  loading: boolean = true;

  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;

  constructor(public dialogRef: MatDialogRef<InsuranceCompanyClientsComponent>,
              @Inject(MAT_DIALOG_DATA) public insuranceCompany: InsuranceCompany,
              public dialog: MatDialog,
              private clientService: ClientService,
              private insuranceCompanyService: InsuranceCompanyService) {

  }

  ngOnInit(): void {
    this.paginator._intl.itemsPerPageLabel = 'Elementos por página';
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  }

  closeClients() {
    this.dialogRef.close();
  }

  deleteClient(element: Client) {
    this.dialog.open(ConfirmDialogComponent, {
      width: '800px',
      data: "¿Esta seguro de que desea eliminar el cliente con dni " + element.dni + " de la empresa aseguradora " + this.insuranceCompany.name + "?"
    })
      .afterClosed()
      .subscribe((confirmed: Boolean) => {
        if (confirmed) {
          this.insuranceCompany.clients.splice()
          this.insuranceCompanyService.update(this.insuranceCompany).subscribe((res) => {
            if (res) {

            }
          })
        }
      });
  }

}
