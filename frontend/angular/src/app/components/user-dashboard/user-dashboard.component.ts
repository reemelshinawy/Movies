import { Component, OnInit } from '@angular/core';
import { MovieService } from '../../services/movie.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Movie } from '../../Models/movie.model';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MovieDetailsDialogComponent } from '../movie-details-dialog/movie-details-dialog.component';

@Component({
  selector: 'app-user-dashboard',
  standalone: true,
  templateUrl: './user-dashboard.component.html',
  styleUrls: ['./user-dashboard.component.scss'],
  imports: [
    CommonModule,
    FormsModule,
    MatInputModule,
    MatCardModule,
    MatButtonModule,
    MatFormFieldModule,
    MatProgressSpinnerModule,
    MatDialogModule,
    MovieDetailsDialogComponent,
  ]
})
export class UserDashboardComponent implements OnInit {
  movies: Movie[] = [];
  filteredMovies: Movie[] = [];
  searchQuery: string = '';
  dbMovies: any[] = [];
  loading: boolean = false;
  ratingInputs: { [movieId: number]: number } = {};
  

  constructor(
    private movieService: MovieService,
    private router: Router,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.getDbUserMovies();
    this.movieService.getAllMovies().subscribe({
      next: (res: Movie[]) => {
        this.movies = res;
        this.filteredMovies = res;
      },
      error: err => console.error('Error fetching movies:', err)
    });
  }

  filterMovies() {
    this.loading = true;
    const query = this.searchQuery.toLowerCase();
    setTimeout(() => {
      this.filteredMovies = this.movies.filter(m =>
        m.title.toLowerCase().includes(query)
      );
      this.currentPage = 1; 
      this.loading = false;
    }, 300);
  }
  

  rateMovie(movieId: number, rating: number | undefined) {
    if (rating == null || rating < 0 || rating > 10) {
      alert('Please enter a rating between 0 and 10.');
      return;
    }

    this.movieService.rateMovie(movieId, rating).subscribe({
      next: () => {
        alert('✅ Movie rated successfully!');
        this.getDbUserMovies();
        this.refreshMovies();
      },
      error: () => alert('Failed to rate movie')
    });
  }

  refreshMovies() {
    this.movieService.getAllMovies().subscribe({
      next: (res: Movie[]) => {
        this.movies = res;
        this.filterMovies();
      },
      error: err => console.error('Error refreshing movies:', err)
    });
  }

  getDbUserMovies() {
    this.movieService.getDbUserMovies().subscribe((res: any) => {
      this.dbMovies = res;
    });
  }

  openMovieDialog(movie: Movie) {
    const dialogRef = this.dialog.open(MovieDetailsDialogComponent, {
      width: '70vw',
      height: '90vh',
      maxWidth: 'none',
      data: { movie }
    });
    
  
    dialogRef.afterClosed().subscribe(result => {
      if (result === 'rated') {
        this.refreshMovies(); // refresh ratings
      }
    });
  }

  currentPage: number = 1;
itemsPerPage: number = 25;

get paginatedMovies(): Movie[] {
  const startIndex = (this.currentPage - 1) * this.itemsPerPage;
  return this.filteredMovies.slice(startIndex, startIndex + this.itemsPerPage);
}

get totalPages(): number {
  return Math.ceil(this.filteredMovies.length / this.itemsPerPage);
}

changePage(page: number) {
  this.currentPage = page;
  window.scrollTo({ top: 0, behavior: 'smooth' });
}
  

  logout() {
    localStorage.clear();
    this.router.navigate(['/']);
  }
}
