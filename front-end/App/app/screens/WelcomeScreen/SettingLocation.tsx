import React, { FC, useState } from "react"
import { ScrollView, View, ViewStyle } from "react-native"
import { Text } from "app/components"
import { WelcomeScreenProps } from "app/navigators/WelcomeNavigator"
import { StepBar } from "app/components/StepBar"
import { BottomStepBar } from "app/components/BottomStepBar"
import { TextToggle } from "app/components/TextToggle"
import { colors } from "app/theme"
import { useHeader } from "app/utils/useHeader"
import { useSafeAreaInsets } from "react-native-safe-area-context"

export const SettingLocationScreen: FC<WelcomeScreenProps<"SettingLocation">> =
  function SettingLocationScreen(_props) {
    const { navigation } = _props
    const { bottom } = useSafeAreaInsets()

    const serviceLocations = [
      "서울특별시",
      "경기도",
      "충청북도",
      "충청남도",
      "전라북도",
      "전라남도",
      "경상북도",
      "경상남도",
    ]

    const [locations, setLocations] = useState<Set<string>>(new Set())

    function goPrev() {
      navigation.navigate("Welcome", { screen: "SettingProfile" })
    }

    function goNext() {
      navigation.navigate("Welcome", { screen: "SettingHobby" })
    }

    useHeader({
      leftIcon: "back",
      leftIconColor: colors.black,
      onLeftPress: goPrev,
    })

    function pressLocation(location: string) {
      if (locations.has(location)) {
        setLocations((prev) => {
          const newLocations = new Set(prev)
          newLocations.delete(location)
          return newLocations
        })
        return
      }

      setLocations(new Set(locations).add(location))
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
          <StepBar currentStep={0.5} />
          <View style={$LocationContainer}>
            <Text size="xl" weight="semiBold">
              모임을 할 장소를 선택해주세요.
            </Text>
            {serviceLocations.map((location) => (
              <TextToggle
                key={location}
                location="left"
                label={location}
                pressed={locations.has(location)}
                onPress={() => {
                  pressLocation(location)
                }}
              />
            ))}
          </View>
        </ScrollView>
        <View style={$KeyboardFixedButton}>
          <BottomStepBar text="다음" onPress={goNext} />
        </View>
      </View>
    )
  }

const $Container: ViewStyle = {
  flex: 1,
  backgroundColor: colors.white,
}

const $LocationContainer: ViewStyle = {
  display: "flex",
  flexDirection: "column",
  paddingHorizontal: 20,
  paddingVertical: 16,
  gap: 16,
}

const $FixedButton: ViewStyle = {
  width: "100%",
  position: "absolute",
}
