import { Component, OnInit } from '@angular/core';
import { MovieService } from '../../services/movie.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.scss'],
  imports: [
    CommonModule,
    FormsModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
  ]
})
export class AdminDashboardComponent implements OnInit {
  searchTitle = '';
  searchResults: any[] = [];
  dbMovies: any[] = [];
  paginatedMovies: any[] = [];

  currentPage = 1;
  moviesPerPage = 20;
  totalPages = 1;
  deletingMovieId: number | null = null;
  filteredDbMovies: any[] = [];


  constructor(
    private movieService: MovieService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.getDbMovies();
  }



  searchMovie() {
    const query = this.searchTitle.trim().toLowerCase();
  
    // Filter DB movies by title
    this.filteredDbMovies = this.dbMovies.filter(movie =>
      movie.title.toLowerCase().includes(query)
    );
    this.totalPages = Math.ceil(this.filteredDbMovies.length / this.moviesPerPage);
    this.changePage(1); 
  
    // API search 
    this.movieService.searchMovie(this.searchTitle).subscribe((res: any) => {
      this.searchResults = res.results || [];
    });
  }
  
  

  addMovie(title: string) {
    this.movieService.addMovie(title).subscribe({
      next: () => {
        this.snackBar.open('Movie added!', 'Close', {
          duration: 3000,
          horizontalPosition: 'center',
          verticalPosition: 'top'
        });

        this.searchTitle = '';
        this.searchResults = [];
  
        this.getDbMovies(true); 
      },
      error: err => {
        const message =
          typeof err.error === 'string'
            ? err.error
            : err.error?.message || ' Failed to add movie';
        this.snackBar.open(message, 'Close', {
          duration: 3000,
          horizontalPosition: 'center',
          verticalPosition: 'top'
        });
      }
      
    });
  }
  
  

  deleteMovie(id: number) {
    const snackRef = this.snackBar.open('Confirm delete?', 'Yes', {
      duration: 5000,
      horizontalPosition: 'center',
      verticalPosition: 'top'
    });

    snackRef.onAction().subscribe(() => {
      this.deletingMovieId = id;

      this.movieService.deleteMovie(id).subscribe({
        next: () => {
          this.snackBar.open('Movie deleted!', 'Close', { duration: 3000 });
          this.getDbMovies();
          this.deletingMovieId = null;
        },
        error: () => {
          this.snackBar.open('Failed to delete movie', 'Close', { duration: 3000 });
          this.deletingMovieId = null;
        }
      });
    });
  }

  getDbMovies(gotoLastPage = false) {
    this.movieService.getDbMovies().subscribe((res: any) => {
      this.dbMovies = res;
      this.filteredDbMovies = res;
  
      this.totalPages = Math.ceil(res.length / this.moviesPerPage);
      const page = gotoLastPage ? this.totalPages : 1;
      this.changePage(page);
    });
  }
  

  changePage(page: number) {
    this.currentPage = page;
    const start = (page - 1) * this.moviesPerPage;
    const end = start + this.moviesPerPage;
    this.paginatedMovies = this.filteredDbMovies.slice(start, end);
  }
  

  logout() {
    localStorage.clear();
    this.router.navigate(['/']);
  }
}