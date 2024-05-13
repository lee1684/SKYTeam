import { Icon, Screen, Text } from "app/components"
import { BottomStepBar } from "app/components/BottomStepBar"
import { useStores } from "app/models"
import { StateContext, TicketInfoScreenProps } from "app/navigators/TicketInfoNavigator"
import { api } from "app/services/api"
import { useHeader } from "app/utils/useHeader"
import React, { FC, useEffect } from "react"
import { Image, ScrollView, View, ViewStyle } from "react-native"

export const TicketInfoScreen: FC<TicketInfoScreenProps<"TicketViewInfo">> =
  function TicketInfoScreen(_props) {
    const {
      authenticationStore: { userInfo },
    } = useStores()
    const { state, setState } = React.useContext(StateContext)

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

    return (
      <Screen preset="fixed" contentContainerStyle={$container} safeAreaEdges={["bottom"]}>
        <ScrollView
          contentContainerStyle={{
            width: "100%",
            minHeight: "100%",
            gap: 16,
            paddingHorizontal: 16,
          }}
        >
          <Text preset="subheading">{state.title}</Text>
          <Text preset="subheading">개최자</Text>
          <View
            style={{
              display: "flex",
              flexDirection: "row",
              gap: 16,
            }}
          >
            <Icon icon="personX2" size={40} />
            <View>
              <Text>{state.creator}</Text>
              <Text preset="accent">인증된 사용자</Text>
            </View>
          </View>
          <Text preset="subheading">모임 날짜 및 장소</Text>
          <View>
            <Text>
              {new Date(state.meetingDate).toLocaleDateString("ko-KR") +
                " " +
                new Date(state.meetingDate).toLocaleTimeString("ko-KR")}
            </Text>
            <Text>{state.location}</Text>
          </View>
          <Text preset="subheading">모임 소개</Text>
          {state.meetingPictureUrls?.length > 0 && (
            <ScrollView
              contentContainerStyle={{
                width: "100%",
                display: "flex",
                flexDirection: "row",
                gap: 16,
              }}
              horizontal
            >
              {state.meetingPictureUrls?.map((url, index) => (
                <Image
                  key={index}
                  style={{ width: 100, height: 100, borderRadius: 12 }}
                  source={{ uri: url }}
                />
              ))}
            </ScrollView>
          )}
          <Text>{state.description}</Text>
          <Text preset="subheading">참가비</Text>
          <Text>{0}원</Text>
          <View>
            <Text preset="subheading">참가자</Text>
            <Text>
              {state.participantIds?.length}명/{state.capacity}명
            </Text>
          </View>
          <ScrollView
            horizontal
            contentContainerStyle={{
              width: "100%",
              display: "flex",
              gap: 12,
            }}
          >
            {state.participantIds?.map((participantId) => (
              <Image
                key={participantId}
                style={{
                  borderRadius: 24,
                }}
                source={{
                  uri: "https://buffer.com/library/content/images/2023/10/free-images.jpg",
                }}
                width={48}
                height={48}
                resizeMode="stretch"
              />
            ))}
          </ScrollView>
        </ScrollView>
        <BottomStepBar onPress={participation} text="참가하기" />
      </Screen>
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
