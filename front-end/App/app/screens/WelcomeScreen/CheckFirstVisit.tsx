import React, { FC, useEffect } from "react"
import { Screen } from "app/components"
import { WelcomeScreenProps } from "app/navigators/WelcomeNavigator"

export const CheckFirstVisitScreen: FC<WelcomeScreenProps<"CheckFirstVisit">> =
  function CheckFirstVisitScreen(_props) {
    const { navigation } = _props

    useEffect(() => {
      navigation.navigate("Welcome", { screen: "SettingProfile" })
    }, [])

    return (
      <Screen
        keyboardShouldPersistTaps="handled"
        preset="scroll"
        safeAreaEdges={["top", "bottom"]}
      ></Screen>
    )
  }
