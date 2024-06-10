import axios, { AxiosInstance } from 'axios';
import { SsalonConfigService } from './ssalon-config.service';
import { Injectable } from '@angular/core';
import { RegisterUserInfo } from '../onboarding/onboarding.component';

<<<<<<< HEAD
export interface Profile {
  id: number;
=======
export const setToken = function (
  token: string,
  apiExecutorService: ApiExecutorService
) {
  apiExecutorService.setToken(token);
};

export interface Profile {
>>>>>>> develop
  nickname: string;
  profilePictureUrl: string;
  gender: 'M' | 'F' | 'G';
  address: string;
  introduction: string;
  interests: string[];
  email: string;
}

<<<<<<< HEAD
export interface ImageGeneration {
  prompt: string;
  highQuality: boolean;
}

=======
>>>>>>> develop
@Injectable({
  providedIn: 'root',
})
export class ApiExecutorService {
  public apiExecutor: AxiosInstance | null = null;
  public apiExecutorJson: AxiosInstance | null = null;
  public apiURL: string = 'https://ssalon.co.kr/api';
<<<<<<< HEAD
  public tokens = {};
  public token: string = '';

  public refreshToken: string = '';
  public myProfile: Profile = undefined as unknown as Profile;
  constructor(private _ssalonConfigService: SsalonConfigService) {
    this.token = '';
    this.initApiExecutor();
  }

  public setToken() {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${'access'}=`);
    this.token = parts.pop()!.split(';').shift()!;
=======
  //public apiURL: string = 'http://localhost:8080/api';
  public tokens = {};
  public token: string = '';
  public refreshToken: string = '';
  public myProfile: Profile = undefined as unknown as Profile;
  constructor(private _ssalonConfigService: SsalonConfigService) {
    this.initApiExecutor();
  }

  public async ngOnInit() {}

  public setToken(token: string) {
    this.token = token;
>>>>>>> develop
  }

  private initApiExecutor() {
    this.apiExecutor = axios.create({
      baseURL: this.apiURL,
      headers: {
        'Content-Type': 'multipart/form-data',
        'Access-Control-Allow-Origin': '*',
<<<<<<< HEAD
=======
        Authorization: `Bearer ${this.token}`,
>>>>>>> develop
        withCredentials: true,
      },
    });
    this.apiExecutorJson = axios.create({
      baseURL: this.apiURL,
      headers: {
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
<<<<<<< HEAD
=======
        Authorization: `Bearer ${this.token}`,
        Refresh: this.refreshToken,
>>>>>>> develop
        withCredentials: true,
      },
    });
    /*
        Authorization: `Bearer ${this.token}`,
        Refresh: this.refreshToken,
    */
  }

  public async getMyProfile() {
    try {
      let response = await this.apiExecutorJson?.get(`/users/me/profile`);
      this.myProfile = response!.data;
<<<<<<< HEAD
    } catch {}
  }

  public async updateMyProfile(body: RegisterUserInfo) {
    try {
      let response = await this.apiExecutorJson?.patch(
        `/users/me/profile`,
        body
      );
      this.myProfile = response!.data;
=======
      console.log(this.myProfile);
>>>>>>> develop
    } catch {}
  }

  public async getLastMessages(moimId: string) {
    try {
      let response = await this.apiExecutor?.get(`/chat-history/${moimId}`);
      console.log(response!.data);
      return response!.data;
    } catch {
      /** dummy data */
      return false;
    }
  }

  public async getChattingParticipants(moimId: string) {
    try {
      let response = await this.apiExecutorJson?.get(`/chat/${moimId}/users`);
      return response!.data;
    } catch {
      return false;
    }
  }

  public async getTicket(moimId: string) {
    try {
      let response = await this.apiExecutor?.get(`/tickets/${moimId}`);
<<<<<<< HEAD
      console.log(response!.data);
=======
>>>>>>> develop
      return response!.data;
    } catch {
      /** dummy data */
      return false;
    }
  }

  public async createTicket(moimId: string, template: string) {
    try {
      console.log('post');
      let response = await this.apiExecutorJson?.post(
        `/tickets/${moimId}?template=${template}`
      );
      return response!.data;
    } catch {
      /** dummy data */
      let tempJson = await axios.get('assets/decoration.json');
      return tempJson.data;
    }
  }

  public async editTicket(moimId: string, body: FormData) {
    try {
      let response = await this.apiExecutor?.put(`/tickets/${moimId}`, body);
      return response!.data;
    } catch (error) {
      console.log(error);
      /** dummy data */
      let tempJson = await axios.get('assets/decoration.json');
      return tempJson.data;
    }
  }

<<<<<<< HEAD
  public async generateImage(moimId: string, body: ImageGeneration) {
    try {
      const response = await this.apiExecutorJson?.post(
        `/image/generate/${moimId}`,
        body
      );
      return response!.data;
    } catch (error) {
      console.log(error);
    }
  }

=======
>>>>>>> develop
  public async uploadTicketImages(moimId: string, body: FormData) {
    try {
      let response = await this.apiExecutor?.post(
        `/tickets/${moimId}/image`,
        body
      );
      return response!.data;
    } catch (error) {
      console.log(error);
      /** dummy data */
      let tempJson = await axios.get('assets/decoration.json');
      return tempJson.data;
    }
  }

  public async createMoimReview(moimId: string, body: any) {
    try {
      let response = await this.apiExecutorJson?.post(
        `/diary/${moimId}/info`,
        body
      );
      return response!.data;
    } catch {
      return false;
    }
  }

  public async getMoimReview(moimId: string) {
    try {
      let response = await this.apiExecutorJson?.get(`/diary/${moimId}/info`);
      console.log(response!.data);
      return response!.data;
    } catch {
      return false;
    }
  }

<<<<<<< HEAD
  public async editMoimReview(moimId: string, body: any) {
    try {
      let response = await this.apiExecutorJson?.post(
        `/diary/${moimId}/info`,
        body
      );
      return response!.data;
    } catch {
      return false;
    }
  }

=======
>>>>>>> develop
  public async getDiary(moimId: string) {
    try {
      let response = await this.apiExecutor?.get(`/diary/${moimId}`);
      return JSON.parse(response!.data.resultJSON);
    } catch {
      /** dummy data */
      return false;
    }
  }

  public async createDiary(moimId: string, template: string) {
    try {
      let response = await this.apiExecutorJson?.post(
        `/diary/${moimId}?template=${template}`
      );
      return response!.data;
    } catch (e) {
      console.log(e);
      /** dummy data */
      let tempJson = await axios.get('assets/decoration.json');
      return tempJson.data;
    }
  }

  public async editDiary(moimId: string, body: FormData) {
    try {
      let response = await this.apiExecutor?.put(`/diary/${moimId}`, body);
      return response!.data;
    } catch (error) {
      console.log(error);
      /** dummy data */
      let tempJson = await axios.get('assets/decoration.json');
      return tempJson.data;
    }
  }

  public async uploadDiaryImages(moimId: string, body: FormData) {
    try {
      let response = await this.apiExecutor?.post(
        `/diary/${moimId}/image`,
        body
      );
      return response!.data;
    } catch (error) {
      console.log(error);
      /** dummy data */
      let tempJson = await axios.get('assets/decoration.json');
      return tempJson.data;
    }
  }

  public async uploadGeneralImage(body: FormData) {
    try {
      let response = await this.apiExecutor?.post(
        `/image-upload/general`,
        body
      );
      return response!.data;
    } catch (error) {
      console.log(error);
      /** dummy data */
      let tempJson = await axios.get('assets/decoration.json');
      return tempJson.data;
    }
  }

<<<<<<< HEAD
  public async getCategorys() {
    try {
      let response = await this.apiExecutorJson?.get('/category/all');
      return response!.data;
    } catch (error) {
      console.log(error);
    }
  }

  public async getRecommendedCategorys() {
    try {
      let response = await this.apiExecutorJson?.get('category/recommend');
      return response!.data;
    } catch (error) {
      console.log(error);
    }
  }

=======
>>>>>>> develop
  public async getBarcode(moimId: string) {
    try {
      let response = await this.apiExecutorJson?.get(`/tickets/${moimId}/link`);
      return response!.data;
    } catch (error) {
      return 'asdfo7a809sd7fwae9089iafa';
    }
  }

  public async checkQR(moimId: string, qrString: string) {
    try {
      let body = { qrKey: qrString };
      let response = await this.apiExecutorJson?.post(
        `/tickets/${moimId}/link`,
        body
      );
      return response!.data;
    } catch {}
  }

  public async changeAttendanceStatus(
    moimId: string,
    userId: number,
    status: boolean = false
  ) {
    try {
      let response = await this.apiExecutorJson?.post(
        `/moims/${moimId}/attendance/${userId}`
      );
      return response!.data;
    } catch {}
  }

  public async getJoiningUsers(moimId: string) {
    try {
      let response = await this.apiExecutorJson?.get(`/moims/${moimId}/users`);
      return response!.data;
    } catch {}
  }

  public async getMoimInfo(moimId: string) {
    try {
      let response = await this.apiExecutorJson?.get(`/moims/${moimId}`);
      return response!.data;
    } catch (e) {
      console.log(e);
    }
  }

  public async editMoimInfo(moimId: string, body: any) {
    try {
      let response = await this.apiExecutorJson?.patch(
        `/moims/${moimId}`,
        body
      );
      return response!.data;
    } catch (e) {
      console.log(e);
    }
  }

  public async getMoims(params: string = '') {
    try {
      let url =
        params === ''
<<<<<<< HEAD
          ? '/moims/home?categoryLen=10&meetingLen=10&categoryPage=1&isEnd=false' //'/moims?size=1000&isEnd=false' //'
=======
          ? '/moims?size=1000&isEnd=false'
>>>>>>> develop
          : `/moims?isEnd=false&category=${params}&size=1000`;
      let response = await this.apiExecutorJson?.get(url);
      return response!.data;
    } catch (e) {
      console.log(e);
    }
  }

<<<<<<< HEAD
  public async getRecommendedMoims(params: string = '') {
    try {
      let url = '/moims/recommend?isEnd=false';
      let response = await this.apiExecutorJson?.get(url);
      return response!.data;
    } catch (e) {
      console.log(e);
    }
  }

=======
>>>>>>> develop
  public async getRunningMoims() {
    try {
      let url = `/moims?isParticipant=true&isEnd=false&size=100`;
      let response = await this.apiExecutorJson?.get(url);
      return response!.data;
    } catch (e) {
      console.log(e);
    }
  }

  public async getEndedMoims() {
    try {
      let url = `/moims?isParticipant=true&isEnd=true&size=100`;
      let response = await this.apiExecutorJson?.get(url);
      return response!.data;
    } catch (e) {
      console.log(e);
    }
  }

<<<<<<< HEAD
  public async searchMoims(keyword: string) {
    try {
      let response = await this.apiExecutorJson?.get(
        `/moims/search/keyword?keyword=${keyword}`
      );
      return response!.data;
    } catch (e) {
      console.log(e);
    }
  }

=======
>>>>>>> develop
  public async joinMoim(moimId: string) {
    try {
      let response = await this.apiExecutorJson?.post(`moims/${moimId}/users`);
      return response?.data;
    } catch (e) {
      return false;
    }
  }

<<<<<<< HEAD
  public async getPaymentinfo(moimId: string) {
    try {
      let response = await this.apiExecutorJson?.get(
        `moims/${moimId}/me/payment`
      );
      return response?.data;
    } catch (e) {
      return false;
    }
  }

  public async getRefund(moimId: string, paymentId: string) {
    try {
      let response = await this.apiExecutorJson?.post(
        `moims/${moimId}/billings/${paymentId}`
      );
      return response?.data;
    } catch (e) {
      return false;
    }
  }

  public async payFee(moimId: string) {
    try {
      let response = await this.apiExecutorJson?.post(
        `moims/${moimId}/billings`
      );
      return response?.data;
    } catch (e) {
      return false;
    }
  }

  public async leaveMoim(moimId: string, myId: string) {
    try {
      let response = await this.apiExecutorJson?.delete(
        `moims/${moimId}/users/${myId}`
      );
      return response?.data;
    } catch (e) {
      return false;
    }
  }

  /** 개최자는 참가자 모두 강퇴 가능, 참가자는 자신만. */
  public async kickParticipant(moimId: string, userId: number, reason: string) {
    try {
      let body = {
        reason: reason,
      };
      let response = await this.apiExecutorJson?.delete(
        `moims/${moimId}/users/${userId}`,
        {
          data: body,
        }
      );
      return response?.data;
    } catch (e) {
      return false;
    }
  }

  public async reportParticipant(
    reporterId: number,
    reportedUserId: number,
    reason: string
  ) {
    try {
      let body = {
        id: 0,
        reporterId: reporterId,
        reportedUserId: reportedUserId,
        reportPictureUrls: [],
        reason: reason,
        isSolved: false,
        reportDate: new Date(),
        solvedDate: '',
      };
      let response = await this.apiExecutorJson?.post(`report`, body);
      return response?.data;
    } catch (e) {
      return false;
    }
  }

  public async exitSsalon() {
    try {
      let response = await this.apiExecutorJson?.delete(`users/me`);
      return response?.data;
    } catch (e) {
      return false;
    }
  }

=======
>>>>>>> develop
  public async checkParticipant(moimId: string) {
    try {
      let response = await this.apiExecutorJson?.get(
        `moims/${moimId}/participant`
      );
      return response!.data;
    } catch (e: any) {
      return false;
    }
  }

  public async checkCreator(moimId: string) {
    try {
      let response = await this.apiExecutorJson?.get(`moims/${moimId}/creator`);
      return response!.data;
    } catch (e: any) {
      return false;
    }
  }

  public async getProfile() {
    try {
      let response = await this.apiExecutorJson?.get(`/users/me/profile`);
      return response!.data;
    } catch (e) {
      return false;
    }
  }

  public async getIsRegister() {
    try {
      let response = await this.apiExecutorJson?.get(`/users/me/signup-verify`);
      return response!.data.isRegistered;
    } catch (e) {
      return false;
    }
  }

  public async createMeeting(body: any) {
    try {
      let response = await this.apiExecutorJson?.post(`/moims`, body);
      return response!.data;
    } catch (e) {
      console.log(e);
    }
  }

<<<<<<< HEAD
  public async removeMeeting(moimId: string) {
    try {
      let response = await this.apiExecutorJson?.delete(`/moims/${moimId}`);
      return response!.data;
    } catch (e) {
      console.log(e);
    }
  }

=======
>>>>>>> develop
  public async registerUser(body: RegisterUserInfo) {
    try {
      let response = await this.apiExecutorJson?.post('/auth/signup', body);
      return response!.data;
    } catch (e) {
      console.log(e);
    }
  }
<<<<<<< HEAD

  public async getUserInfoByEmail(email: string) {
    try {
      let response = await this.apiExecutorJson?.get(
        `/users/email/profile?email=${email}`
      );
      return response!.data;
    } catch (e) {
      console.log(e);
    }
  }

  public async removeAccount() {
    try {
      let response = await this.apiExecutorJson?.delete(`/users/me`);
      return response!.data;
    } catch (e) {
      console.log(e);
    }
  }

  public async logout() {
    try {
      let response = await this.apiExecutorJson?.delete(`/auth/logout`);
      return response!.data;
    } catch (e) {
      console.log(e);
    }
  }
=======
>>>>>>> develop
}
