import { Screen } from "app/components"
import { BottomStepBar } from "app/components/BottomStepBar"
import Config from "app/config"
import { useStores } from "app/models"
import { StateContext, TicketInfoScreenProps } from "app/navigators/TicketInfoNavigator"
import { api } from "app/services/api"
import { useHeader } from "app/utils/useHeader"
import React, { FC, useContext, useEffect } from "react"
import { Image, ScrollView, View, ViewStyle } from "react-native"
import { TouchableOpacity } from "react-native-gesture-handler"
import { useSafeAreaInsets } from "react-native-safe-area-context"
import WebView from "react-native-webview"

export const TicketViewScreen: FC<TicketInfoScreenProps<"TicketViewLook">> =
  function TicketViewScreen(_props) {
    const {
      authenticationStore: { userInfo },
    } = useStores()

    const { route, navigation } = _props
    const { bottom } = useSafeAreaInsets()
    const { state, setState } = useContext(StateContext)

    const [isTicketClicked, setIsTicketClicked] = React.useState(false)

    const $container: ViewStyle[] = [$subContainer, { paddingBottom: bottom }]

    function participation() {
      api.postMoimUser(state.id).then((response) => {
        if (response.kind === "ok") {
          alert("참가 신청이 완료되었습니다.")
          setState((prev) => ({
            ...prev,
            participantIds: [...prev.participantIds, userInfo.nickname],
          }))
        }
      })
    }

    useEffect(() => {
      api.getMoimDetail(route.params.moimId).then((response) => {
        setState(response.data)
      })
    }, [])

    return (
      <>
        {isTicketClicked ? (
          <WebView
            startInLoadingState
            javaScriptEnabled
            mixedContentMode="compatibility"
            source={{ uri: Config.API_URL + `?moimId=${state.id}&viewType=view` }}
          />
        ) : (
          <Screen preset="fixed" contentContainerStyle={$container}>
            <ScrollView
              contentContainerStyle={{
                flex: 1,
              }}
            >
              <View
                style={{
                  paddingVertical: 40,
                  flex: 1,
                  display: "flex",
                  flexDirection: "column",
                  justifyContent: "center",
                  alignItems: "center",
                  gap: 16,
                }}
              >
                <TouchableOpacity
                  onPress={() => {
                    setIsTicketClicked(true)
                  }}
                >
                  <View
                    style={{
                      position: "absolute",
                      top: 0,
                      left: 0,
                      borderRadius: 8,
                      aspectRatio: 7 / 12,
                      backgroundColor: "black",
                      width: 320,
                    }}
                  />
                  <Image
                    style={{
                      resizeMode: "stretch",
                      flex: 1,
                      aspectRatio: 7 / 12,
                    }}
                    source={{
                      uri: `https://test-bukkit-240415.s3.ap-northeast-2.amazonaws.com/Thumbnails/${route.params.moimId}/Thumb-${route.params.moimId}.png`,
                    }}
                  />
                </TouchableOpacity>
              </View>
            </ScrollView>
            <BottomStepBar onPress={participation} text="참가하기" />
          </Screen>
        )}
      </>
    )
  }

const $subContainer: ViewStyle = {
  flex: 1,
  display: "flex",
  flexDirection: "column",
  justifyContent: "space-between",
}
