import { Instance, SnapshotOut, types } from "mobx-state-tree"

interface UserInfo {
  nickname?: string | null
  gender?: string | null
  introduction?: string | null
}

export const AuthenticationStoreModel = types
  .model("AuthenticationStore")
  .props({
    authToken: types.maybe(types.string),
    userInfo: types.frozen(),
  })
  .views((store) => ({
    get isAuthenticated() {
      return !!store.authToken
    },
  }))
  .actions((store) => ({
    setAuthToken(value?: string) {
      store.authToken = value
    },
    setUserInfo(value: UserInfo) {
      store.userInfo = value
    },
    logout() {
      store.authToken = undefined
    },
  }))

export interface AuthenticationStore extends Instance<typeof AuthenticationStoreModel> {}
export interface AuthenticationStoreSnapshot extends SnapshotOut<typeof AuthenticationStoreModel> {}
