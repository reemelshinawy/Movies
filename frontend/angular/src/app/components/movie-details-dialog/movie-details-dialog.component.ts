import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MovieService } from '../../services/movie.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';

@Component({
  selector: 'app-movie-details-dialog',
  standalone: true,
  templateUrl: './movie-details-dialog.component.html',
  styleUrls: ['./movie-details-dialog.component.scss'],
  imports: [
    CommonModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatDialogModule 
  ]
})
export class MovieDetailsDialogComponent {
  rating: number | null = null;
  movie: any; 

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    public dialogRef: MatDialogRef<MovieDetailsDialogComponent>,
    private movieService: MovieService
  ) {
    this.movie = data.movie; 
    console.log('[Dialog Movie Details]', this.movie);

    this.rating = data.movie.userRating ?? null;
  }

  submitRating() {
    if (this.rating != null && this.rating >= 0 && this.rating <= 10) {
      this.movieService.rateMovie(this.movie.id, this.rating).subscribe(() => {
        this.dialogRef.close('rated');
      });
    }
  }
}
