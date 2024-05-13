import { CompositeScreenProps } from "@react-navigation/native"
import React from "react"
import { AppStackParamList, AppStackScreenProps } from "./AppNavigator"
import { NativeStackScreenProps, createNativeStackNavigator } from "@react-navigation/native-stack"
import { ProfileScreen, SettingScreen } from "app/screens"

export type ProfileParamList = {
  Profile: undefined
  Setting: undefined
}

export type ProfileScreenProps<T extends keyof ProfileParamList> = CompositeScreenProps<
  NativeStackScreenProps<ProfileParamList, T>,
  AppStackScreenProps<keyof AppStackParamList>
>

const Stack = createNativeStackNavigator<ProfileParamList>()

export function ProfileNavigator() {
  return (
    <Stack.Navigator
      screenOptions={{
        headerShown: false,
      }}
    >
      <Stack.Screen name="Profile" component={ProfileScreen} />
      <Stack.Screen name="Setting" component={SettingScreen} />
    </Stack.Navigator>
  )
}
