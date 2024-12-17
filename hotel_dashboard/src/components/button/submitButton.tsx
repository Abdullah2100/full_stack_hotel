import { CircularProgress } from "@mui/material";
import { enStatus } from "../../module/enState";

interface submitButtonProps {
    placeHolder: string;
    onSubmit: () => void;
    buttonStatus?: enStatus | undefined;
    style?:string|undefined
}

const SubmitButton = (
    {
        onSubmit,
        placeHolder,
        buttonStatus = enStatus.none,
        style =undefined
    }: submitButtonProps) => {
        return <button
        className={style}
        onSubmit={onSubmit}>
            {
             buttonStatus ===enStatus.loading?
             <CircularProgress sx={{ color: 'white' }} size={10} className="mt-1" />
             :placeHolder

            }
        </button>

}
export default SubmitButton;