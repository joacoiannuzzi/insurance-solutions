import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {User} from "../../../../shared/models/user";
import {MatSort} from "@angular/material/sort";
import {MatPaginator} from "@angular/material/paginator";
import {MatDialog} from "@angular/material/dialog";
import {UserService} from "../../../../shared/services/user.service";
import {UserAddComponent} from "../user-add/user-add.component";
import {InsuranceCompany} from "../../../../shared/models/insuranceCompany";

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.scss']
})
export class UserListComponent implements OnInit, AfterViewInit {
  displayedColumns: string[] = ['username', 'insuranceCompany', 'options'];
  users: User[];
  dataSource: MatTableDataSource<User> = new MatTableDataSource<User>([]);
  loading: boolean = true;

  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;

  constructor(private userService: UserService, public dialog: MatDialog) { }

  ngOnInit(): void {
    this.getUsers();
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    this.paginator._intl.itemsPerPageLabel = 'Elementos por página';
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    this.dataSource.paginator?.firstPage();
  }

  getUsers() {
    this.loading = true;
    this.userService.users.subscribe((data) => {
      this.users = data;
      this.loading = false;
      this.dataSource.data = this.users;
    });
  }

  openDialog() {
    const dialogRef = this.dialog.open(UserAddComponent, {
      width: '800px',
      data: new User('', -1, "", "", "", new InsuranceCompany(-2, '', []))
    });

    dialogRef.afterClosed().subscribe(() => {
      this.getUsers();
    });
  }

  deleteUser(user: User) {
    // this.dialog.open(ConfirmDialogComponent, {
    //   data: "¿Está seguro de que desea eliminar al usuario " + User.name + "?"
    // })
    //   .afterClosed()
    //   .subscribe((confirmed: boolean) => {
    //     if (confirmed) {
    //       this.userService.delete(user).subscribe(() => {
    //         this.getUsers();
    //       });
    //     }
    //   })
  }

  updateUser(user: User) {
    // const dialogRef = this.dialog.open(UserUpdateComponent, {
    //   width: '800px',
    //   data: user
    // });
    // dialogRef.afterClosed().subscribe((res) => {
    //   if (res) {
    //     this.getUsers();
    //   }
    // })
  }

  openUserDetails(element: User): void {
    // const dialogRef = this.dialog.open(UserDetailsComponent, {
    //   width: '800px',
    //   data: element
    // });
    // dialogRef.afterClosed().subscribe(() => {
    //   this.getUsers();
    // })
  }

}
