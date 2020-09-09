import {Component} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';

/**
 * @title Dialog with header, scrollable content and actions
 */
@Component({
 selector: 'app-delete-confirmation',
  templateUrl: './delete-confirmation.component.html',
  styleUrls: ['./delete-confirmation.component.scss']
})
export class DeleteConfirmationComponent {
  constructor(public dialog: MatDialog) {}

  }

