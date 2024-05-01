import { Injectable } from '@angular/core';
import axios, { AxiosInstance } from 'axios';
import { DecorationInfo, SsalonConfigService } from './ssalon-config.service';
@Injectable({
  providedIn: 'root',
})
export class ApiExecutorService {
  public apiExecutor: AxiosInstance | null = null;
  public apiURL: string = 'http://3.34.0.190:8080/api';
  public token: string =
    'eyJhbGciOiJIUzI1NiJ9.eyJjYXRlZ29yeSI6ImFjY2VzcyIsInVzZXJuYW1lIjoibmF2ZXIgbHphV19oUmprc1kzZXo1NUtJckpXdE9mMk1qTi1GZzJJbUF5SXBPOFNlcyIsInJvbGUiOiJST0xFX1VTRVIiLCJpYXQiOjE3MTQ1NDgyOTYsImV4cCI6MTcxNDU0ODg5Nn0.buWg9S7G4l4pwr7VVZ1nYCOby9UpKEcbyukp6HEy0dc';
  public refreshToken: string = `eyJhbGciOiJIUzI1NiJ9.eyJjYXRlZ29yeSI6InJlZnJlc2giLCJ1c2VybmFtZSI6Im5hdmVyIGx6YVdfaFJqa3NZM2V6NTVLSXJKV3RPZjJNak4tRmcySW1BeUlwTzhTZXMiLCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNzE0NTQ4Mjk2LCJleHAiOjE3MTQ2MzQ2OTZ9.NnY1N7eaLhMwinp8V9foecFHh7VlR8o038tPkjtTO60`;

  constructor(private _ssalonConfigService: SsalonConfigService) {
    this.initApiExecutor();
  }

  private initApiExecutor() {
    this.apiExecutor = axios.create({
      baseURL: this.apiURL,
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${this.token}`,
        Refresh: this.refreshToken,
      },
    });
  }

  public async getTicket() {
    try {
      let response = await this.apiExecutor?.post(
        `/tickets/${this._ssalonConfigService.MOIM_ID}`
      );
      return response!.data;
    } catch {
      /** dummy data */
      let tempJson = await axios.get('assets/decoration.json');
      return tempJson.data;
    }
  }

  public async editTicket(body: DecorationInfo) {
    try {
      let response = await this.apiExecutor?.put(
        `/tickets/${this._ssalonConfigService.MOIM_ID}`,
        body
      );
      return response!.data;
    } catch {
      /** dummy data */
      let tempJson = await axios.get('assets/decoration.json');
      return tempJson.data;
    }
  }

  public async getDiary() {
    try {
      let response = await this.apiExecutor?.post(
        `/diary/${this._ssalonConfigService.DIARY_ID}`
      );
      return response!.data;
    } catch {
      /** dummy data */
      let tempJson = await axios.get('assets/decoration.json');
      return tempJson.data;
    }
  }

  public async editDiary(diaryId: string) {}
}
