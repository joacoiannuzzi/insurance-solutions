import {MonitoringSystemUpdateComponent} from '../monitoring-system-update/monitoring-system-update.component';
import {MonitoringSystem} from '../../../../shared/models/monitoringSystem';
import {MonitoringSystemAddComponent} from '../monitoring-system-add/monitoring-system-add.component';
import {MonitoringSystemService} from '../../../../shared/services/monitoring-system.service';
import {Component, OnInit, AfterViewInit, ViewChild} from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {MatDialog} from '@angular/material/dialog';
import {MatSort} from '@angular/material/sort';
import {MatPaginator} from '@angular/material/paginator';
import {ConfirmDialogComponent} from '../../../components/confirm-dialog/confirm-dialog.component';
import {MonitoringSystemDetailsComponent} from "../monitoring-system-details/monitoring-system-details.component";
import {Sensor} from "../../../../shared/models/sensor";

@Component({
  selector: 'app-monitoring-system-list',
  templateUrl: './monitoring-system-list.component.html',
  styleUrls: ['./monitoring-system-list.component.scss']
})
export class MonitoringSystemListComponent implements OnInit, AfterViewInit {

  displayedColumns: string[] = ["name", "sensor", "monitoringCompany", "assigned", "options"];
  monitoringSystems: MonitoringSystem[];
  dataSource: MatTableDataSource<MonitoringSystem> = new MatTableDataSource<MonitoringSystem>();
  loading: boolean = true;

  constructor(private monitoringSystemService: MonitoringSystemService, public dialog: MatDialog) {
  }

  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  ngOnInit(): void {
    this.getMonitoringSystems();
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    this.paginator._intl.itemsPerPageLabel = 'Elementos por página';
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  getMonitoringSystems() {
    this.loading = true;
    this.monitoringSystemService.monitoringSystems.subscribe((data) => {
      this.monitoringSystems = data;
      this.loading = false;
      this.dataSource.data = this.monitoringSystems;
    })
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(MonitoringSystemAddComponent, {
      width: '1000px',
      data: new MonitoringSystem(null, "", new Sensor(0, '',''), "", false, null)
    });
    dialogRef.afterClosed().subscribe((res) => {
      if (res) {
        this.getMonitoringSystems();
      }
    });
  }

  deleteMonitoringSystem(moSys: MonitoringSystem) {
    this.dialog.open(ConfirmDialogComponent, {
      data: "¿Está seguro que desea eliminar el servicio " + moSys.name + "?"
    })
      .afterClosed()
      .subscribe((confirmed: boolean) => {
        if (confirmed) {
          this.monitoringSystemService.delete(moSys).subscribe(() => {
            this.getMonitoringSystems();
          });
        }
      })
  }

  openMonitoringSystemDetails(element: MonitoringSystem) {
    const dialogRef = this.dialog.open(MonitoringSystemDetailsComponent, {
      width: '1000px',
      data: element
    });
    dialogRef.afterClosed().subscribe((res) => {
      if (res) {
        this.getMonitoringSystems();
      }
    });
  }

  updateMonitoringSystem(moSys: MonitoringSystem) {
    const dialogRef = this.dialog.open(MonitoringSystemUpdateComponent, {
      width: '800px',
      data: moSys
    });
    dialogRef.afterClosed().subscribe((confirmed) => {
      if (confirmed) {
        this.getMonitoringSystems();
      }
    })
  }
}
