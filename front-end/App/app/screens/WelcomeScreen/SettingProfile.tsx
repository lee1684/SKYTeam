import React, { FC, useEffect, useState } from "react"
import {
  ImageStyle,
  InputAccessoryView,
  Keyboard,
  ScrollView,
  TextStyle,
  View,
  ViewStyle,
} from "react-native"
import { Icon, Text, TextField } from "app/components"
import { colors } from "../../theme"
import { WelcomeScreenProps } from "app/navigators/WelcomeNavigator"
import { StepBar } from "app/components/StepBar"
import { TextToggle } from "app/components/TextToggle"
import { BottomStepBar } from "app/components/BottomStepBar"
import { useSafeAreaInsets } from "react-native-safe-area-context"
import { useHeader } from "app/utils/useHeader"

export const SettingProfileScreen: FC<WelcomeScreenProps<"SettingProfile">> =
  function SettingProfileScreen(_props) {
    const { bottom } = useSafeAreaInsets()
    const [isKeyboardVisible, setKeyboardVisible] = useState(false)

    useEffect(() => {
      const keyboardDidShowListener = Keyboard.addListener("keyboardWillShow", () => {
        setKeyboardVisible(true)
      })
      const keyboardDidHideListener = Keyboard.addListener("keyboardDidHide", () => {
        setKeyboardVisible(false)
      })

      return () => {
        keyboardDidHideListener.remove()
        keyboardDidShowListener.remove()
      }
    }, [])

    const [gender, setGender] = useState<string>("")
    const { navigation } = _props

    function goNext() {
      navigation.navigate("Welcome", { screen: "SettingLocation" })
    }

    useHeader({
      leftIcon: "back",
      leftIconColor: colors.palette.gray100,
    })

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
          <StepBar currentStep={0.13} />
          <View style={$profileContainer}>
            <View>
              <Text size="xl" weight="semiBold">
                프로필을 작성해주세요.
              </Text>
            </View>
            <Icon style={$avatar} icon="personX2" size={96} />

            <TextField
              inputAccessoryViewID="next"
              label={
                <Text size="xs" weight="medium">
                  닉네임
                  <Text style={$important} size="xs" weight="medium">
                    (필수)
                  </Text>
                </Text>
              }
              placeholder="닉네임을 입력해주세요"
            />

            <View style={$CheckBoxContainer}>
              <Text size="xs">성별</Text>
              <View style={$CheckBox}>
                <TextToggle
                  divide
                  label="남자"
                  pressed={gender === "남자"}
                  onPress={() => setGender("남자")}
                />
                <TextToggle
                  divide
                  label="여자"
                  pressed={gender === "여자"}
                  onPress={() => setGender("여자")}
                />
                <TextToggle
                  divide
                  label="기타"
                  pressed={gender === "기타"}
                  onPress={() => setGender("기타")}
                />
              </View>
            </View>
            <TextField
              inputAccessoryViewID="next"
              label={
                <Text size="xs" weight="medium">
                  소개
                </Text>
              }
              placeholder="간단한 소개를 입력해주세요"
            />
          </View>
        </ScrollView>

        <InputAccessoryView nativeID="next">
          <BottomStepBar text="다음" onPress={goNext} />
        </InputAccessoryView>
        {!isKeyboardVisible && (
          <View style={$KeyboardFixedButton}>
            <BottomStepBar text="다음" onPress={goNext} />
          </View>
        )}
      </View>
    )
  }

const $Container: ViewStyle = {
  flex: 1,
  backgroundColor: colors.white,
}

const $profileContainer: ViewStyle = {
  display: "flex",
  flexDirection: "column",
  paddingHorizontal: 20,
  paddingVertical: 16,
  gap: 16,
}

const $avatar: ImageStyle = {
  alignSelf: "center",
  marginVertical: 24,
}

const $important: TextStyle = {
  color: colors.palette.blue,
}

const $CheckBoxContainer: ViewStyle = {
  display: "flex",
  flexDirection: "column",
  gap: 12,
}

const $CheckBox: ViewStyle = {
  display: "flex",
  flexDirection: "row",
  justifyContent: "space-between",
  gap: 12,
}

const $FixedButton: ViewStyle = {
  width: "100%",
  position: "absolute",
}
