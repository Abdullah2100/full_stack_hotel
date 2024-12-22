import { createSlice, PayloadAction } from "@reduxjs/toolkit";

interface iAuthState{
    token?:string,
    refreshToken?:string
}

const initialState:iAuthState = {
    token:undefined,
    refreshToken:undefined
}

const jwtSlice = createSlice({
    name: 'auth',
    initialState,  // Fix the typo here
    reducers: {
      // Action to set tokens
      setTokens: (state, action: PayloadAction<{ accessToken: string|undefined; refreshToken: string|undefined}>) => {
        state.token = action.payload.accessToken;
        state.refreshToken = action.payload.refreshToken;
      },
    },
  });


  export const { setTokens } = jwtSlice.actions;  // Export the action creator
  export default jwtSlice.reducer;  