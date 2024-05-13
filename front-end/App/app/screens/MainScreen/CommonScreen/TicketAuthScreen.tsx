import { Icon, Screen, Text } from "app/components"
import { BottomStepBar } from "app/components/BottomStepBar"
import { WebViewScreen } from "app/components/WebViewScreen"
import Config from "app/config"
import { useStores } from "app/models"
import { StateContext, TicketInfoScreenProps } from "app/navigators/TicketInfoNavigator"
import { api } from "app/services/api"
import { useHeader } from "app/utils/useHeader"
import React, { FC, useEffect } from "react"
import { Image, ScrollView, View, ViewStyle } from "react-native"
import WebView from "react-native-webview"

export const TicketAuthScreen: FC<TicketInfoScreenProps<"TicketAuth">> = function TicketAuthScreen(
  _props,
) {
  const {
    authenticationStore: { userInfo, authToken },
  } = useStores()
  const { state, setState } = React.useContext(StateContext)
  const [isCreator, setIsCreator] = React.useState(false)

  useEffect(() => {
    if (state.id) {
      api.getMoimIsCreator(state.id).then((response) => {
        setIsCreator(response.data)
      })
    }
  }, [state.id])

  return (
    <WebViewScreen>
      {isCreator ? (
        <WebView
          source={{
            uri: Config.API_URL + `?moimId=${state.id}&viewType=qrcheck&token=${authToken}`,
          }}
        />
      ) : (
        <WebView
          source={{
            uri: Config.API_URL + `?moimId=${state.id}&viewType=qrshow&token=${authToken}`,
          }}
        />
      )}
    </WebViewScreen>
  )
}

const $container: ViewStyle = {
  width: "100%",
  height: "auto",
  display: "flex",
  flexDirection: "column",
  flexWrap: "wrap",
  gap: 16,
}
