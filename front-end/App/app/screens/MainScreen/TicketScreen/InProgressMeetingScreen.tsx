import { Screen } from "app/components"
import { TicketTabScreenProps } from "app/navigators/TicketNavigator"
import { api } from "app/services/api"
import React, { FC, useEffect } from "react"
import { Image, TouchableOpacity, View, ViewStyle } from "react-native"

export const InProgressMeetingScreen: FC<TicketTabScreenProps<"InProgressMeeting">> =
  function InProgressMeetingScreen(_props) {
    const { navigation } = _props

    useEffect(() => {
      api.getMoimJoin
    }, [])
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
          <TouchableOpacity
            onPress={() => {
              navigation.navigate("TicketView", { screen: "TicketViewLook" })
            }}
            style={{
              flex: 1,
              aspectRatio: 7 / 12,
            }}
          >
            <Image
              style={{
                flex: 1,
                aspectRatio: 7 / 12,
                resizeMode: "stretch",
              }}
              source={require("assets/icons/ticket-big.png")}
            />
          </TouchableOpacity>
          <TouchableOpacity
            onPress={() => {
              navigation.navigate("TicketView", { screen: "TicketViewLook" })
            }}
            style={{
              flex: 1,
              aspectRatio: 7 / 12,
            }}
          >
            <Image
              style={{
                flex: 1,
                aspectRatio: 7 / 12,
                resizeMode: "stretch",
              }}
              source={require("assets/icons/ticket-big.png")}
            />
          </TouchableOpacity>
        </View>
        <View
          style={{
            flex: 1,
            display: "flex",
            flexDirection: "row",
            justifyContent: "space-between",

            gap: 16,
          }}
        >
          <TouchableOpacity
            onPress={() => {
              navigation.navigate("TicketView", { screen: "TicketViewLook" })
            }}
            style={{
              flex: 1,
              aspectRatio: 7 / 12,
            }}
          >
            <Image
              style={{
                flex: 1,
                aspectRatio: 7 / 12,
                resizeMode: "stretch",
              }}
              source={require("assets/icons/ticket-big.png")}
            />
          </TouchableOpacity>
          <TouchableOpacity
            onPress={() => {
              navigation.navigate("TicketView", { screen: "TicketViewLook" })
            }}
            style={{
              flex: 1,
              aspectRatio: 7 / 12,
            }}
          >
            <Image
              style={{
                flex: 1,
                aspectRatio: 7 / 12,
                resizeMode: "stretch",
              }}
              source={require("assets/icons/ticket-big.png")}
            />
          </TouchableOpacity>
        </View>
      </Screen>
    )
  }

const $container: ViewStyle = {
  display: "flex",
  flexDirection: "column",
  flexWrap: "wrap",
  gap: 16,
  paddingHorizontal: 16,
  paddingVertical: 20,
}
