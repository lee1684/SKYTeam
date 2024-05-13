import React, { FC, useEffect, useState } from "react"
import {
  Image,
  ImageStyle,
  ScrollView,
  TextStyle,
  TouchableOpacity,
  View,
  ViewStyle,
} from "react-native"
import { Card, Icon, Screen, Text } from "../../components"
import { MainTabScreenProps } from "../../navigators/MainNavigator"
import { useHeader } from "app/utils/useHeader"
import { api } from "app/services/api"

export const HomeScreen: FC<MainTabScreenProps<"Home">> = function HomeScreen(_props) {
  const { navigation } = _props
  const theme = ["운동", "독서", "요리", "여행", "음악", "스터디", "쇼핑", "예술", "사진", "게임"]
  const order = "RECENT"
  const [data, setData] = useState([[], [], [], [], [], [], [], [], [], []])

  useEffect(() => {
    function fetchData(category) {
      api
        .getMoim(new URLSearchParams({ category, order, page: "0", size: "10" }))
        .then((response) => {
          setData((prev) => {
            const temp = [...prev]
            temp[theme.indexOf(category)] = response.data?.content
            return temp
          })
        })
    }

    theme.forEach((item) => {
      fetchData(item)
    })
  }, [])

  useHeader({
    title: (
      <Image resizeMode="contain" style={$logo} source={require("assets/icons/salon-logo.png")} />
    ),
    titleMode: "flex",
    titleContainerStyle: {
      flex: 1,
      justifyContent: "center",
      alignItems: "flex-start",
    },
  })

  return (
    <Screen preset="fixed" contentContainerStyle={$container}>
      <ScrollView
        horizontal
        showsHorizontalScrollIndicator={false}
        contentContainerStyle={$themeContainer}
      >
        {theme?.map((item, index) => (
          <Card key={index} style={$themeCard} content={item} contentStyle={$themeCardTitle} />
        ))}
      </ScrollView>
      <ScrollView
        contentContainerStyle={{
          display: "flex",
          flexDirection: "column",
          gap: 20,
        }}
      >
        {data?.map((item, index) => (
          <View
            key={index}
            style={{
              gap: 20,
            }}
          >
            <Text>{theme[index]}</Text>
            <ScrollView
              horizontal
              showsHorizontalScrollIndicator={false}
              contentContainerStyle={{
                gap: 10,
              }}
            >
              {item.map((item2) => {
                return (
                  <TouchableOpacity
                    key={item2.moimId}
                    onPress={() => {
                      navigation.navigate("TicketView", {
                        screen: "TicketViewLook",
                        params: { moimId: item2.moimId },
                      })
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
                        borderRadius: 8,
                        resizeMode: "stretch",
                        aspectRatio: 7 / 12,
                      }}
                      width={320}
                      source={{ uri: item2.ticketThumb }}
                    />
                  </TouchableOpacity>
                )
              })}
            </ScrollView>
          </View>
        ))}
      </ScrollView>
    </Screen>
  )
}

const $container: ViewStyle = {
  paddingLeft: 20,
  gap: 20,
}

const $logo: ImageStyle = {
  width: 81,
  height: 28,
}

const $themeContainer: ViewStyle = {
  display: "flex",
  flexDirection: "row",
  gap: 10,
  maxHeight: 33,
  marginBottom: 20,
}
const $themeCard: ViewStyle = {
  width: 49,
  height: 33,
  borderRadius: 4,
  padding: 0,
  display: "flex",
  justifyContent: "center",
  alignItems: "center",
}

const $themeCardTitle: TextStyle = {
  textAlign: "center",
}
