interface TextInputProps {
   keyType:string;
   placeHolder: string;
   isDisabled?: boolean;
   isMultipleLine?: boolean;
   maxLength?: number;
   onInput: (value: any, key: string) => void;
   style?: string;
   type?: React.HTMLInputTypeAttribute;
   value: any;
 }
 
 export const TextInput = ({
   keyType='',
   placeHolder,
   isDisabled = false,
   maxLength,
   onInput,
   style = '',
   type = 'text',
   isMultipleLine = false,
   value,
 }: TextInputProps) => {
   const generalStyle = `text-black px-2 border-gray border-2 border-r-2 rounded-[3px] text-[12px] focus:rounded-[2px] ${style}`;
   
   // Function to handle input change and call onInput with value and key
   const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
     onInput(e.target.value, keyType); // Use name attribute to determine the key
   };
 
   if (isMultipleLine) {
     return (
       <textarea
         name={placeHolder} // Assign the key based on placeholder
         value={value}
         className={generalStyle}
         placeholder={placeHolder}
         maxLength={maxLength}
         disabled={isDisabled}
         onChange={handleChange}
       />
     );
   }
 
   return (
     <input
       name={placeHolder} // Assign the key based on placeholder
       value={value}
       className={`h-7 ${generalStyle}`}
       placeholder={placeHolder}
       maxLength={maxLength}
       disabled={isDisabled}
       onChange={handleChange}
       type={type}
     />
   );
 };
 