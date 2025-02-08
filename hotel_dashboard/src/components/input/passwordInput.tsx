import { EyeIcon, EyeSlashIcon } from "@heroicons/react/16/solid";
import { FocusEventHandler, forwardRef, useState } from "react";

interface TextInputProps {
    keyType: string;
    placeHolder: string;
    isDisabled?: boolean;
    isMultipleLine?: boolean;
    maxLength?: number;
    onInput: (value: any, key: string) => void;
    style?: string;
    type?: React.HTMLInputTypeAttribute;
    value: any;
    isRequire?: boolean | undefined;
    onFoucseInpur?: FocusEventHandler<HTMLInputElement | HTMLTextAreaElement> | undefined
    isHasTitle?: boolean | undefined
    canShowOrHidePassowrd?: boolean | undefined
}

export const PasswordInput =
    ({
        keyType = '',
        placeHolder,
        isDisabled = false,
        maxLength,
        onInput,
        style = '',

        value,
        isRequire = false,
        onFoucseInpur = undefined,
        isHasTitle = true
        , canShowOrHidePassowrd = undefined,

    }: TextInputProps
    ) => {
        const [passwordType, setType] = useState<React.HTMLInputTypeAttribute>('password')
    const generalStyle = `text-black px-2 border-gray border-2 border-r-2 rounded-[3px] text-[12px] focus:rounded-[2px]  `+style;

        // Function to handle input change and call onInput with value and key
        const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
            onInput(e.target.value, keyType); // Use name attribute to determine the key
        };



        return (
            <div className="">
                {isHasTitle &&
                    <h6 className="text-[10px]  mb-[0.5px]">{keyType}</h6>}
                {
                    <div>
                        {
                            canShowOrHidePassowrd === true &&
                            <div className="w-[200px]  relative">

                                {passwordType === 'text' ?
                                    <button
                                        onClick={() => setType('password')}
                                        className="absolute h-5 w-5 end-10 top-1">
                                        <EyeIcon fontSize={5} />
                                    </button> :
                                    <button
                                        onClick={() => setType("text")}

                                        className="absolute h-5 w-5 end-10 top-1">
                                        <EyeSlashIcon />
                                    </button>
                                }
                            </div>
                        }
                        <input
                            name={placeHolder} // Assign the key based on placeholder
                            value={value}
                            className={`h-7 ${generalStyle}`}
                            placeholder={placeHolder}
                            maxLength={maxLength}
                            disabled={isDisabled}
                            onChange={handleChange}
                            type={passwordType}
                            required={isRequire}
                            onFocus={onFoucseInpur}
                        />
                    </div>
                }
            </div>

        );
    };
