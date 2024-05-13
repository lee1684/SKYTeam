/**
 * These types indicate the shape of the data you expect to receive from your
 * API endpoint, assuming it's a JSON object like we have.
 */
export interface EpisodeItem {
  title: string
  pubDate: string
  link: string
  guid: string
  author: string
  thumbnail: string
  description: string
  content: string
  enclosure: {
    link: string
    type: string
    length: number
    duration: number
    rating: { scheme: string; value: string }
  }
  categories: string[]
}

export interface ApiFeedResponse {
  status: string
  feed: {
    url: string
    title: string
    link: string
    author: string
    description: string
    image: string
  }
  items: EpisodeItem[]
}

/**
 * The options used to configure apisauce.
 */
export interface ApiConfig {
  /**
   * The URL of the api.
   */
  url: string

  /**
   * Milliseconds before we timeout the request.
   */
  timeout: number
}

export interface PostMoimRequestBody {
  category: string
  meetingPictureUrls: string[]
  title: string
  description: string
  location: string
  capacity: number
  meetingDate: Date
  payment: number
  isSharable: boolean
}

export interface UserInfo {
  address?: string | null
  gender?: string | null
  interest?: string[]
  introduction?: string | null
  memberDates?: Date[] | null
  nickname?: string | null
  profilePictureUrl?: string | null
}

export interface PostSignUpRequestBody {
  nickname: string
  profilePictureUrl: string
  gender: string
  address: string
  introduction: string
  interests: string[]
}
