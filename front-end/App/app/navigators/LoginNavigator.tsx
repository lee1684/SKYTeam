import { CompositeScreenProps } from "@react-navigation/native"
import React from "react"
import { AppStackParamList, AppStackScreenProps } from "./AppNavigator"
import { NativeStackScreenProps, createNativeStackNavigator } from "@react-navigation/native-stack"
import { NaverScreen } from "app/screens/LoginScreen/NaverScreen"
import { LoginScreen } from "app/screens/LoginScreen/LoginScreen"

export type LoginParamList = {
  Login: undefined
  Google: undefined
  Kakao: undefined
  Naver: undefined
}

export type LoginScreenProps<T extends keyof LoginParamList> = CompositeScreenProps<
  NativeStackScreenProps<LoginParamList, T>,
  AppStackScreenProps<keyof AppStackParamList>
>

const Stack = createNativeStackNavigator<LoginParamList>()

export function LoginNavigator() {
  return (
    <Stack.Navigator
      screenOptions={{
        headerShown: false,
      }}
    >
      {/* <Stack.Screen name="Google" component={SettingProfileScreen} />
      <Stack.Screen name="Kakao" component={SettingLocationScreen} /> */}
      <Stack.Screen name="Login" component={LoginScreen} />
      <Stack.Screen name="Naver" component={NaverScreen} />
    </Stack.Navigator>
  )
}
