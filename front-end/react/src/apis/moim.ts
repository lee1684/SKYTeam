import { instance } from '.';

export const MoimApi = {
  getMoimList: async () => {
    const { data } = await instance.get('admin/moims', {
      headers: {
        Authorization: `Bearer ${'eyJhbGciOiJIUzI1NiJ9.eyJjYXRlZ29yeSI6ImFjY2VzcyIsInVzZXJuYW1lIjoia2FrYW8gMzQ1ODI3NjMwMyIsInJvbGUiOiJST0xFX0FETUlOIiwiaWF0IjoxNzE3NDA2OTY3LCJleHAiOjE3MTc0OTMzNjd9.Px0QV5CXIsfxN1Xu9b1-NAPSzCRGpUu9QtE-TGbKIuc'}`,
      },
    });

    return data;
  },
  deleteMoim: async ({ moimId }: { moimId: number }) => {
    await instance.delete(`admin/moims/${moimId}`, {
      headers: {
        Authorization: `Bearer ${'eyJhbGciOiJIUzI1NiJ9.eyJjYXRlZ29yeSI6ImFjY2VzcyIsInVzZXJuYW1lIjoia2FrYW8gMzQ1ODI3NjMwMyIsInJvbGUiOiJST0xFX0FETUlOIiwiaWF0IjoxNzE3NDA2OTY3LCJleHAiOjE3MTc0OTMzNjd9.Px0QV5CXIsfxN1Xu9b1-NAPSzCRGpUu9QtE-TGbKIuc'}`,
      },
    });
  },
};
