import React, { FC, useEffect, useState } from "react"
import { InputAccessoryView, Keyboard, ScrollView, View, ViewStyle } from "react-native"
import { Icon, Screen, Text, TextField } from "../../components"
import { MainTabScreenProps } from "../../navigators/MainNavigator"
import { useHeader } from "app/utils/useHeader"
import { BottomStepBar } from "app/components/BottomStepBar"
import { useSafeAreaInsets } from "react-native-safe-area-context"
import { api } from "app/services/api"
import RNPickerSelect from "react-native-picker-select"
import DatePicker from "react-native-date-picker"
import { WebViewScreen } from "app/components/WebViewScreen"
import WebView from "react-native-webview"
import Config from "app/config"
import { useStores } from "app/models"

export const CreateTicketScreen: FC<MainTabScreenProps<"CreateTicket">> =
  function CreateTicketScreen(_props) {
    const {
      authenticationStore: { authToken },
    } = useStores()
    const { navigation } = _props
    const { bottom } = useSafeAreaInsets()
    const [categories, setCategories] = useState<string>("select")
    const [title, setTitle] = useState<string>("")
    const [dateOpen, setDateOpen] = useState<boolean>(false)
    const [date, setDate] = useState<Date>(new Date())
    const [location, setLocation] = useState<string>("")
    const [description, setDescription] = useState<string>("")
    const [capacity, setCapacity] = useState<string>("")
    const [payment, setPayment] = useState<string>("")
    const [buttonClicked, setButtonClicked] = useState<string>("")
    const theme = ["운동", "독서", "요리", "여행", "음악", "스터디", "쇼핑", "예술", "사진", "게임"]
    useHeader({
      leftIcon: "back",
      onLeftPress: () => {
        navigation.goBack()
      },
      title: "모임 만들기",
    })

    function onPressNext() {
      api
        .postMoim({
          category: categories,
          meetingPictureUrls: [],
          title,
          description,
          location,
          capacity: capacity === null ? 0 : Number(capacity),
          meetingDate: date,
          payment: payment === null ? 0 : Number(payment),
          isSharable: true,
        })
        .then((response) => {
          setButtonClicked(response.data.id)
        })
    }

    const $container: ViewStyle[] = [$subContainer, { paddingBottom: bottom }]

    return (
      <>
        {buttonClicked ? (
          <WebViewScreen>
            <WebView
              source={{
                uri:
                  Config.API_URL +
                  `?moimId=${buttonClicked}}&viewType=edit&face=front&token=${authToken}`,
              }}
            />
          </WebViewScreen>
        ) : (
          <View style={$container}>
            <ScrollView
              keyboardDismissMode="interactive"
              keyboardShouldPersistTaps="handled"
              automaticallyAdjustKeyboardInsets={true}
              automaticallyAdjustContentInsets={true}
              contentInsetAdjustmentBehavior="never"
              contentContainerStyle={{
                backgroundColor: "white",
                gap: 16,
                paddingHorizontal: 16,
              }}
            >
              <RNPickerSelect
                onValueChange={(value) => setCategories(value)}
                items={theme.map((category) => {
                  return {
                    label: category,
                    value: category,
                  }
                })}
              />
              <TextField
                inputAccessoryViewID="create"
                label={
                  <Text size="xs" weight="medium">
                    모임 이름
                  </Text>
                }
                placeholder="모임 이름을 작성해주세요"
                value={title}
                onChangeText={setTitle}
              />
              <>
                <TextField
                  status="disabled"
                  inputAccessoryViewID="create"
                  label={
                    <Text size="xs" weight="medium">
                      날짜
                    </Text>
                  }
                  value={date.toLocaleDateString("ko-KR") + " " + date.toLocaleTimeString("ko-KR")}
                  RightAccessory={() => (
                    <Icon style={{}} icon="bell" onPress={() => setDateOpen(true)} />
                  )}
                  placeholder="모임이 진행될 날짜를 선택해주세요"
                />
                <DatePicker
                  modal
                  open={dateOpen}
                  date={date}
                  onConfirm={(date) => {
                    setDate(date)
                    setDateOpen(false)
                  }}
                  onCancel={() => setDateOpen(false)}
                  locale="ko-KR"
                />
              </>
              <TextField
                inputAccessoryViewID="create"
                label={
                  <Text size="xs" weight="medium">
                    장소
                  </Text>
                }
                placeholder="장소를 입력해주세요"
                value={location}
                onChangeText={setLocation}
              />
              <TextField
                inputAccessoryViewID="create"
                label={
                  <Text size="xs" weight="medium">
                    모임 소개
                  </Text>
                }
                placeholder="소개를 입력해주세요"
                value={description}
                onChangeText={setDescription}
              />
              <View>
                <Text>사진(선택)</Text>
                <Text>모임 설명에 도움이 되는 사진을 등록해주세요</Text>
                <ScrollView
                  horizontal
                  contentContainerStyle={{
                    display: "flex",
                    flexDirection: "row",
                    gap: 16,
                  }}
                >
                  <View
                    style={{
                      width: 100,
                      height: 100,
                      backgroundColor: "gray",
                      borderRadius: 12,
                    }}
                  />
                  <View
                    style={{
                      width: 100,
                      height: 100,
                      backgroundColor: "gray",
                      borderRadius: 12,
                    }}
                  />
                  <View
                    style={{
                      width: 100,
                      height: 100,
                      backgroundColor: "gray",
                      borderRadius: 12,
                    }}
                  />
                  <View
                    style={{
                      width: 100,
                      height: 100,
                      backgroundColor: "gray",
                      borderRadius: 12,
                    }}
                  />
                  <View
                    style={{
                      width: 100,
                      height: 100,
                      backgroundColor: "gray",
                      borderRadius: 12,
                    }}
                  />
                  <View
                    style={{
                      width: 100,
                      height: 100,
                      backgroundColor: "gray",
                      borderRadius: 12,
                    }}
                  />
                </ScrollView>
              </View>

              <TextField
                inputAccessoryViewID="create"
                label={
                  <Text size="xs" weight="medium">
                    최대 인원수
                  </Text>
                }
                placeholder="최대 인원수를 입력해주세요"
                value={capacity}
                onChangeText={setCapacity}
              />
              <TextField
                inputAccessoryViewID="create"
                label={
                  <Text size="xs" weight="medium">
                    참가비
                  </Text>
                }
                placeholder="참가비를 입력해주세요"
                value={payment}
                onChangeText={setPayment}
              />
            </ScrollView>
            <BottomStepBar onPress={onPressNext} text="다음" />
            <InputAccessoryView nativeID="create">
              <BottomStepBar onPress={onPressNext} text="다음" />
            </InputAccessoryView>
          </View>
        )}
      </>
    )
  }

const $subContainer: ViewStyle = {
  flex: 1,
  backgroundColor: "white",
  gap: 16,
}
