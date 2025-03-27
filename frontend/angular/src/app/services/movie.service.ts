import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { forkJoin, Observable } from 'rxjs';
import { Movie } from '../Models/movie.model';


@Injectable({ providedIn: 'root' })
export class MovieService {
  private baseUrl = 'http://localhost:9090/api';

  constructor(private http: HttpClient) {}



  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('jwt');
    return new HttpHeaders({
      'Authorization': token ? `Bearer ${token}` : ''
    });
  }
  

  searchMovie(query: string) {
    return this.http.get<any>(`${this.baseUrl}/admin/search?title=${query}`, {
      headers: this.getAuthHeaders()
    });
  }



  addMovie(imdbID: string) {
    return this.http.post(`${this.baseUrl}/admin/movies?imdbID=${imdbID}`, {}, {
      headers: this.getAuthHeaders()
    });
  }
  

  deleteMovie(id: number) {
    return this.http.delete(`${this.baseUrl}/admin/movies/${id}`, {
      headers: this.getAuthHeaders()
    });
  }

  getDbMovies() {
    return this.http.get<any[]>(`${this.baseUrl}/admin/movies`, {
      headers: this.getAuthHeaders()
    });
  }
  getDbUserMovies() {
    return this.http.get<any[]>(`${this.baseUrl}/user/movies`, {
      headers: this.getAuthHeaders()
      
    });
  }

  getAllMovies(): Observable<Movie[]> {
    return this.http.get<Movie[]>(`${this.baseUrl}/user/movies`, {
      headers: this.getAuthHeaders()
    });
  }
  
  rateMovie(movieId: number, rating: number) {
    return this.http.post(`${this.baseUrl}/user/movies/${movieId}/rate?rating=${rating}`, {}, {
      headers: this.getAuthHeaders()
    });
  }
  
    

}
