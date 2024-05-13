import React, { FC, useState } from "react"
import { ImageStyle, View, ViewStyle } from "react-native"
import { Button, Icon, Screen, Text, TextField } from "../../../components"
import { useStores } from "app/models"
import { useHeader } from "app/utils/useHeader"
import { TextToggle } from "app/components/TextToggle"
import { ProfileScreenProps } from "app/navigators/ProfileNavigator"
import { colors } from "app/theme"
import { api } from "app/services/api"

export const ProfileScreen: FC<ProfileScreenProps<"Profile">> = function ProfileScreen(_props) {
  const { navigation } = _props
  const {
    authenticationStore: { userInfo },
  } = useStores()

  const [originNickname, setOriginNickname] = useState<string>(userInfo.nickname)
  const [originGender, setOriginGender] = useState<string>(userInfo.gender)
  const [originIntroduction, setOriginIntroduction] = useState<string>(userInfo.introduction)

  const [name, setName] = useState<string>(originNickname)
  const [gender, setGender] = useState<string>(originGender)
  const [introduction, setIntroduction] = useState<string>(originIntroduction)

  useHeader({
    title: "프로필",
    titleMode: "flex",
    titleContainerStyle: {
      alignItems: "flex-start",
    },
    rightIcon: "settings",
    onRightPress: () => {
      navigation.navigate("Setting")
    },
  })

  function saveProfile() {
    api
      .patchUserInfo({
        nickname: name,
        gender,
        introduction,
      })
      .then((response) => {
        if (response.kind === "ok") {
          setOriginNickname(name)
          setOriginGender(gender)
          setOriginIntroduction(introduction)
        }
      })
  }

  return (
    <Screen preset="scroll" contentContainerStyle={$container}>
      <Icon style={$avatar} icon="personX2" size={96} />

      <TextField
        label={
          <Text size="xs" weight="medium">
            이름/닉네임
          </Text>
        }
        placeholder="닉네임을 입력해주세요"
        value={name}
        onChangeText={setName}
      />

      <View style={$CheckBoxContainer}>
        <Text size="xs">성별</Text>
        <View style={$CheckBox}>
          <TextToggle divide label="남자" pressed={gender === "M"} onPress={() => setGender("M")} />
          <TextToggle divide label="여자" pressed={gender === "F"} onPress={() => setGender("F")} />
          <TextToggle divide label="기타" pressed={gender === "G"} onPress={() => setGender("G")} />
        </View>
        <TextField
          label={
            <Text size="xs" weight="medium">
              소개
            </Text>
          }
          placeholder="간단한 소개를 입력해주세요"
          value={introduction}
          onChangeText={setIntroduction}
        />
      </View>
      <View
        style={{
          display: "flex",
          flexDirection: "row",
          justifyContent: "flex-end",
        }}
      >
        <Button
          onPress={saveProfile}
          style={{
            backgroundColor: "transparent",
            borderColor: "transparent",
          }}
          disabledTextStyle={{ color: colors.palette.gray200 }}
          disabled={
            originNickname === name &&
            originGender === gender &&
            originIntroduction === introduction
          }
        >
          저장
        </Button>
      </View>
    </Screen>
  )
}

const $container: ViewStyle = {
  paddingHorizontal: 20,
  gap: 16,
  flex: 1,
}

const $avatar: ImageStyle = {
  alignSelf: "center",
  marginVertical: 24,
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
