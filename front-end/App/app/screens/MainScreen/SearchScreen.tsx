import React, { FC } from "react"
import { View, ViewStyle } from "react-native"
import { MainTabScreenProps } from "../../navigators/MainNavigator"
import { useHeader } from "app/utils/useHeader"
import { WebViewScreen } from "app/components/WebViewScreen"
import WebView from "react-native-webview"
import { Screen } from "app/components"
export const SearchScreen: FC<MainTabScreenProps<"Search">> = function SearchScreen(_props) {
  useHeader({
    title: "검색",
  })
  return <View></View>
}

const $container: ViewStyle = {
  flex: 1,
}
