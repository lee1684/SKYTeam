import React, { FC, useEffect } from "react"
import { Screen } from "app/components"
import { WelcomeScreenProps } from "app/navigators/WelcomeNavigator"
import { api } from "app/services/api"
import { useStores } from "app/models"

export const CheckFirstVisitScreen: FC<WelcomeScreenProps<"CheckFirstVisit">> =
  function CheckFirstVisitScreen(_props) {
    const {
      authenticationStore: { setUserInfo, setAuthToken },
    } = useStores()
    const { navigation } = _props

    useEffect(() => {
      api.getUserInfo().then((response) => {
        if (response.kind === "ok") {
          const userInfo = response.user

          if (userInfo.memberDates === null || userInfo.nickname === null) {
            navigation.navigate("Welcome", { screen: "SettingProfile" })
          } else {
            setUserInfo({
              nickname: userInfo.nickname,
              gender: userInfo.gender,
              introduction: userInfo.introduction,
            })
            navigation.navigate("Main", { screen: "Home" })
          }
        } else {
          setAuthToken("")
        }
      })
    }, [])

    return (
      <Screen
        keyboardShouldPersistTaps="handled"
        preset="scroll"
        safeAreaEdges={["top", "bottom"]}
      ></Screen>
    )
  }
