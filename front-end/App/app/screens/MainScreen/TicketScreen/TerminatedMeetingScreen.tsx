import { Screen } from "app/components"
import { TicketTabScreenProps } from "app/navigators/TicketNavigator"
import React, { FC } from "react"
import { Image, View, ViewStyle } from "react-native"

export const TerminatedMeetingScreen: FC<TicketTabScreenProps<"TerminatedMeeting">> =
  function TerminatedMeetingScreen(_props) {
    return (
      <Screen preset="scroll" contentContainerStyle={$container}>
        <View
          style={{
            flex: 1,
            display: "flex",
            flexDirection: "row",
            justifyContent: "space-between",

            gap: 16,
          }}
        >
          <Image
            style={{
              resizeMode: "stretch",
              flex: 1,
              aspectRatio: 7 / 12,
            }}
            source={require("assets/icons/ticket-big.png")}
          />
          <Image
            style={{
              resizeMode: "stretch",
              flex: 1,
              aspectRatio: 7 / 12,
            }}
            source={require("assets/icons/ticket-big.png")}
          />
        </View>
      </Screen>
    )
  }

const $container: ViewStyle = {
  height: "auto",
  display: "flex",
  flexDirection: "column",
  flexWrap: "wrap",
  gap: 16,
  paddingHorizontal: 16,
  paddingVertical: 20,
}
