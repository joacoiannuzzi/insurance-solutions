import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {Sensor} from "../../../../shared/models/sensor";
import {MatSort} from "@angular/material/sort";
import {MatPaginator} from "@angular/material/paginator";
import {MatDialog} from "@angular/material/dialog";
import {SensorService} from "../../../../shared/services/sensor.service";
import {SensorAddComponent} from "../sensor-add/sensor-add.component";
import {SensorUpdateComponent} from "../sensor-update/sensor-update.component";

@Component({
  selector: 'app-sensor-list',
  templateUrl: './sensor-list.component.html',
  styleUrls: ['./sensor-list.component.scss']
})
export class SensorListComponent implements OnInit, AfterViewInit {

  displayedColumns: string[] = ['name', 'model', 'monitoringSystem', 'options'];
  sensors: Sensor[];
  dataSource: MatTableDataSource<Sensor> = new MatTableDataSource<Sensor>([]);
  loading: boolean = true;

  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;

  constructor(private sensorService: SensorService, public dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.getSensors();
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

  getSensors() {
    this.loading = true;
    this.sensorService.sensors.subscribe((sensors) => {
      this.sensors = sensors;
      this.loading = false;
      this.dataSource.data = this.sensors;
    });
  }

  createSensor(): void {
    const dialogRef = this.dialog.open(SensorAddComponent, {
      width: '800px',
      data: new Sensor(null, '', '', [])
    });

    dialogRef.afterClosed().subscribe(() => {
      this.getSensors();
    });
  }

  deleteSensor(sensor: Sensor) {
  //   // this.dialog.open(ConfirmDialogComponent, {
  //   //   data: "¿Está seguro de que desea eliminar al usuario " + Sensor.name + "?"
  //   // })
  //   //   .afterClosed()
  //   //   .subscribe((confirmed: boolean) => {
  //   //     if (confirmed) {
  //   //       this.sensorService.delete(sensor).subscribe(() => {
  //   //         this.getSensors();
  //   //       });
  //   //     }
  //   //   })
  }

  updateSensor(sensor: Sensor) {
    const dialogRef = this.dialog.open(SensorUpdateComponent, {
      width: '800px',
      data: sensor
    });
    dialogRef.afterClosed().subscribe((res) => {
      if (res) {
        this.getSensors();
      }
    })
  }

  openSensorDetails(sensor: Sensor): void {
    // const dialogRef = this.dialog.open(SensorDetailsComponent, {
    //   width: '800px',
    //   data: sensor
    // });
    // dialogRef.afterClosed().subscribe(() => {
    //   this.getSensors();
    // })
  }

}
