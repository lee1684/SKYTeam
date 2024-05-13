import React, { FC, useState } from "react"
import { ScrollView, View, ViewStyle } from "react-native"
import { Text } from "app/components"
import { WelcomeScreenProps } from "app/navigators/WelcomeNavigator"
import { StepBar } from "app/components/StepBar"
import { BottomStepBar } from "app/components/BottomStepBar"
import { TextToggle } from "app/components/TextToggle"
import { useHeader } from "app/utils/useHeader"
import { colors } from "app/theme"
import { useSafeAreaInsets } from "react-native-safe-area-context"
import { api } from "app/services/api"

export const SettingHobbyScreen: FC<WelcomeScreenProps<"SettingHobby">> =
  function SettingHobbyScreen(_props) {
    const { navigation, route } = _props
    const { bottom } = useSafeAreaInsets()

    const serviceCategories = ["운동", "게임", "음악", "음식", "독서", "영화"]

    const [categories, setCategories] = useState<Set<string>>(new Set())

    function goPrev() {
      navigation.navigate("Welcome", { screen: "SettingLocation" })
    }

    function goNext() {
      api
        .postSignUp({
          ...(route.params as unknown as {
            nickname: string
            profilePictureUrl: string
            gender: string
            address: string
            introduction: string
            interests: string[]
          }),
          interests: Array.from(categories),
        })
        .then((response) => {
          if (response.kind === "ok") {
            navigation.navigate("Main", { screen: "Home" })
          } else {
            alert("회원가입에 실패했습니다.")
          }
        })
    }

    useHeader({
      leftIcon: "back",
      leftIconColor: colors.black,
      onLeftPress: goPrev,
    })

    function pressCategory(location: string) {
      if (categories.has(location)) {
        setCategories((prev) => {
          const newLocations = new Set(prev)
          newLocations.delete(location)
          return newLocations
        })
        return
      }

      setCategories(new Set(categories).add(location))
    }

    const $KeyboardFixedButton = [
      $FixedButton,
      {
        bottom,
      },
    ]

    return (
      <View style={$Container}>
        <ScrollView
          keyboardDismissMode="interactive"
          keyboardShouldPersistTaps="handled"
          automaticallyAdjustKeyboardInsets={true}
          automaticallyAdjustContentInsets={true}
          contentInsetAdjustmentBehavior="never"
          contentContainerStyle={{
            paddingBottom: bottom,
          }}
        >
          <StepBar currentStep={0.98} />
          <View style={$HobbyContainer}>
            <Text size="xl" weight="semiBold">
              관심 있는 카테고리를 선택해주세요.
            </Text>
            {serviceCategories.map((category) => (
              <TextToggle
                key={category}
                location="left"
                label={category}
                pressed={categories.has(category)}
                onPress={() => {
                  pressCategory(category)
                }}
              />
            ))}
          </View>
        </ScrollView>
        <View style={$KeyboardFixedButton}>
          <BottomStepBar text="시작하기" onPress={goNext} />
        </View>
      </View>
    )
  }
const $Container: ViewStyle = {
  flex: 1,
  backgroundColor: colors.white,
}

const $HobbyContainer: ViewStyle = {
  display: "flex",
  flexDirection: "column",
  paddingHorizontal: 20,
  paddingVertical: 16,
  gap: 16,
}

const $FixedButton: ViewStyle = {
  position: "absolute",
  width: "100%",
}
