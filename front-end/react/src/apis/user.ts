import { instance } from '.';

export const UserApi = {
  getUserList: async () => {
    const { data } = await instance.get('admin/users', {
      headers: {
        Authorization: `Bearer ${'eyJhbGciOiJIUzI1NiJ9.eyJjYXRlZ29yeSI6ImFjY2VzcyIsInVzZXJuYW1lIjoia2FrYW8gMzQ1ODI3NjMwMyIsInJvbGUiOiJST0xFX0FETUlOIiwiaWF0IjoxNzE3NDA2OTY3LCJleHAiOjE3MTc0OTMzNjd9.Px0QV5CXIsfxN1Xu9b1-NAPSzCRGpUu9QtE-TGbKIuc'}`,
      },
    });

    return data;
  },
  deleteUser: async ({ userId }: { userId: number }) => {
    await instance.delete(`admin/users/${userId}`, {
      headers: {
        Authorization: `Bearer ${'eyJhbGciOiJIUzI1NiJ9.eyJjYXRlZ29yeSI6ImFjY2VzcyIsInVzZXJuYW1lIjoia2FrYW8gMzQ1ODI3NjMwMyIsInJvbGUiOiJST0xFX0FETUlOIiwiaWF0IjoxNzE3NDA2OTY3LCJleHAiOjE3MTc0OTMzNjd9.Px0QV5CXIsfxN1Xu9b1-NAPSzCRGpUu9QtE-TGbKIuc'}`,
      },
    });
  },
};
